package dev.niuren.ic;

import com.google.gson.*;
import dev.niuren.BetaX;
import dev.niuren.systems.modules.Module;
import dev.niuren.systems.modules.Modules;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Configs extends Thread implements Wrapper {
    public final File MAIN_FOLDER = new File(BetaX.MOD_ID);
    private final String MODULES_FOLDER = MAIN_FOLDER.getAbsolutePath() + "/modules";
    private final String prefix = "prefix.json";
    private final String friends = "friends.json";

    private static final List<Runnable> preLoadTasks = new ArrayList<>(1);


    public static Configs INSTANCE;

    public Configs() {
        INSTANCE = this;
    }

    public void addPreLoadTask(Runnable task) {
        preLoadTasks.add(task);
    }

    public void load() {
        try {
            loadPrefix();
            loadModules();
            loadFriends();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Runnable task : preLoadTasks) task.run();
    }

    private void loadModules() throws IOException {
        for (Module m : Modules.get().getModules()) {
            loadModule(m);
        }
    }

    @SuppressWarnings("unchecked")
    private void loadModule(Module m) throws IOException {
        Path path = Path.of(MODULES_FOLDER, m.getName() + ".json");
        if (!Files.exists(path)) return; // 检查文件是否存在

        String rawJson = loadFile(path.toFile());
        if (rawJson == null || rawJson.trim().isEmpty()) return; // 如果文件内容为空，直接返回

        try {
            JsonObject jsonObject = JsonParser.parseString(rawJson).getAsJsonObject();

            if (jsonObject.get("Enabled") != null && jsonObject.get("Drawn") != null && jsonObject.get("Bind") != null) {
                if (jsonObject.get("Enabled").getAsBoolean()) m.toggle();
                m.setDrawn(jsonObject.get("Drawn").getAsBoolean());
                m.setBind(jsonObject.get("Bind").getAsInt());
            }

            Settings.get().getSettingsForMod(m).forEach(s -> {
                JsonElement settingObject = jsonObject.get(s.getName());
                if (settingObject != null) {
                    switch (s.getType()) {
                        case Boolean -> s.setValue(settingObject.getAsBoolean());
                        case Double -> s.setValue(settingObject.getAsDouble());
                        case Mode -> s.setValue(settingObject.getAsString());
                        case Integer -> s.setValue(settingObject.getAsInt());
                    }
                }
            });
        } catch (JsonSyntaxException e) {
            // 如果解析 JSON 出现错误，可以记录日志并忽略该文件
            System.err.println("Failed to parse JSON for module " + m.getName() + ": " + e.getMessage());
        }
    }

    //  private void loadModule(Module m) throws IOException {
    //        Path path = Path.of(MODULES_FOLDER, m.getName() + ".json");
    //        if (!path.toFile().exists()) return;
    //        String rawJson = loadFile(path.toFile());
    //        JsonObject jsonObject = JsonParser.parseString(rawJson).getAsJsonObject();
    //
    //        if (jsonObject.get("Enabled") != null && jsonObject.get("Drawn") != null && jsonObject.get("Bind") != null) {
    //            if (jsonObject.get("Enabled").getAsBoolean()) m.toggle();
    //            m.setDrawn(jsonObject.get("Drawn").getAsBoolean());
    //            m.setBind(jsonObject.get("Bind").getAsInt());
    //        }
    //
    //        Settings.get().getSettingsForMod(m).forEach(s -> {
    //            JsonElement settingObject = jsonObject.get(s.getName());
    //            if (settingObject != null) {
    //                switch (s.getType()) {
    //                    case Boolean -> s.setValue(settingObject.getAsBoolean());
    //                    case Double -> s.setValue(settingObject.getAsDouble());
    //                    case Mode -> s.setValue(settingObject.getAsString());
    //                    case Integer -> s.setValue(settingObject.getAsInt());
    //                }
    //            }
    //        });
    //    }

    private void loadFriends() throws IOException {
        Path path = Path.of(MAIN_FOLDER.getAbsolutePath(), friends);
        if (!path.toFile().exists()) return;
        String rawJson = loadFile(path.toFile());
        JsonObject jsonObject = new JsonParser().parse(rawJson).getAsJsonObject();
        if (jsonObject.get("friends") != null) {
            JsonArray friendObject = jsonObject.get("friends").getAsJsonArray();
            friendObject.forEach(object -> Friends.get().getFriends().add(object.getAsString()));
        }
    }

    private void loadPrefix() throws IOException {
        Path path = Path.of(MAIN_FOLDER.getAbsolutePath(), prefix);
        if (!path.toFile().exists()) return;
        String rawJson = loadFile(path.toFile());
        JsonObject jsonObject = new JsonParser().parse(rawJson).getAsJsonObject();

        if (jsonObject.get("prefix") != null) {
            Command.setPrefix(jsonObject.get("prefix").getAsString());
        }
    }

    public String loadFile(File file) throws IOException {
        FileInputStream stream = new FileInputStream(file.getAbsolutePath());
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }

    @Override
    public void run() {
        if (!MAIN_FOLDER.exists() && !MAIN_FOLDER.mkdirs()) BetaX.LOG.error("Failed to create config folder");
        if (!new File(MODULES_FOLDER).exists() && !new File(MODULES_FOLDER).mkdirs())
            BetaX.LOG.error("Failed to create modules folder");
        try {
            saveModules();
            saveFriends();
            savePrefix();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveModules() throws IOException {
        for (Module m : Modules.get().getModules()) {
            saveModule(m);
        }
    }

    private void saveModule(Module m) throws IOException {
        Path path = Path.of(MODULES_FOLDER, m.getName() + ".json");
        createFile(path);
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("Enabled", new JsonPrimitive(m.isActive()));
        jsonObject.add("Drawn", new JsonPrimitive(m.isDrawn()));
        jsonObject.add("Bind", new JsonPrimitive(m.getBind()));
        Settings.get().getSettingsForMod(m).forEach(s -> {
            switch (s.getType()) {
                case Mode -> jsonObject.add(s.getName(), new JsonPrimitive((String) s.get()));
                case Boolean -> jsonObject.add(s.getName(), new JsonPrimitive((Boolean) s.get()));
                case Double -> jsonObject.add(s.getName(), new JsonPrimitive((Double) s.get()));
                case Integer -> jsonObject.add(s.getName(), new JsonPrimitive((Integer) s.get()));
            }
        });
        Files.write(path, gson.toJson(new JsonParser().parse(jsonObject.toString())).getBytes());
    }

    private void saveFriends() throws IOException {
        Path path = Path.of(MAIN_FOLDER.getAbsolutePath(), friends);
        createFile(path);
        JsonObject jsonObject = new JsonObject();
        JsonArray friends = new JsonArray();
        Friends.get().getFriends().forEach(friends::add);
        jsonObject.add("friends", friends);
        Files.write(path, gson.toJson(new JsonParser().parse(jsonObject.toString())).getBytes());
    }

    private void savePrefix() throws IOException {
        Path path = Path.of(MAIN_FOLDER.getAbsolutePath(), prefix);
        createFile(path);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("prefix", new JsonPrimitive(Command.getPrefix()));
        Files.write(path, gson.toJson(new JsonParser().parse(jsonObject.toString())).getBytes());
    }

    private void createFile(Path path) {
        if (Files.exists(path)) new File(path.normalize().toString()).delete();
        try {
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
