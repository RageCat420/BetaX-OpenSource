package dev.niuren.ic;

import dev.niuren.systems.modules.Module;

import java.util.List;
import java.util.function.Predicate;

public class Setting<T> {
    private final String name;
    private final Module parent;
    private final Module.Category category;
    private final Type type;

    private T value, max, min;
    public Predicate<T> visible;
    private int inc;
    private List<String> modes;

    private String description;

    public boolean isVisible() {
        if (this.visible == null) {
            return true;
        }
        return visible.test(get());
    }

    public void setVisible(Predicate<T> visible) {
        this.visible = visible;
    }

    public Setting(final String name, final Module parent, T value) {
        this.name = name;
        this.parent = parent;
        this.type = Type.Boolean;
        this.category = parent.getCategory();
        this.value = value;

    }
    public Setting(final String name, final Module parent, T value,boolean string) {
        this.name = name;
        this.parent = parent;
        this.type = Type.String;
        this.category = parent.getCategory();
        this.value = value;

    }

    public Setting(final String name, final Module parent, T value, Predicate<T> visible) {
        this.name = name;
        this.parent = parent;
        this.type = Type.Boolean;
        this.category = parent.getCategory();
        this.value = value;
        this.visible = visible;
    }

    public Setting(final String name, final Module parent, T value, T min, T max, int inc, boolean integer) {
        this.name = name;
        this.parent = parent;
        this.type = Type.Integer;
        this.category = parent.getCategory();
        this.value = value;
        this.min = min;
        this.max = max;
        this.inc = inc;
    }
    public Setting(final String name, final Module parent, T value, T min, T max, int inc, boolean integer,Predicate<T> visible) {
        this.name = name;
        this.parent = parent;
        this.type = Type.Integer;
        this.category = parent.getCategory();
        this.value = value;
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.visible = visible;
    }


    public Setting(final String name, final Module parent, T value, T min, T max, int inc) {
        this.name = name;
        this.parent = parent;
        this.type = Type.Double;
        this.category = parent.getCategory();
        this.value = value;
        this.min = min;
        this.max = max;
        this.inc = inc;
    }
    public Setting(final String name, final Module parent, T value, T min, T max, int inc,Predicate<T> visible) {
        this.name = name;
        this.parent = parent;
        this.type = Type.Double;
        this.category = parent.getCategory();
        this.value = value;
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.visible = visible;
    }

/*    public Setting(String name, Module parent, T value, boolean page) {
        this.name = name;
        this.parent = parent;
        this.type = Type.Page;
        this.category = parent.getCategory();
        this.value = value;
    }
    public Setting(String name, Module parent, T value, boolean page,Predicate<T> visible) {
        this.name = name;
        this.parent = parent;
        this.type = Type.Page;
        this.category = parent.getCategory();
        this.value = value;
        this.visible = visible;
    }*/

    public Setting(final String name, final Module parent, List<String> modes, T value) {
        this.name = name;
        this.parent = parent;
        this.type = Type.Mode;
        this.category = parent.getCategory();
        this.value = value;
        this.modes = modes;
    }
    public Setting(final String name, final Module parent, List<String> modes, T value,Predicate<T> visible) {
        this.name = name;
        this.parent = parent;
        this.type = Type.Mode;
        this.category = parent.getCategory();
        this.value = value;
        this.modes = modes;
        this.visible = visible;
    }

    public Setting(final String name, final Module parent) {
        this.name = name;
        this.parent = parent;
        this.type = Type.Description;
        this.category = parent.getCategory();
    }
    public Setting(final String name, final Module parent,Predicate<T> visible) {
        this.name = name;
        this.parent = parent;
        this.type = Type.Description;
        this.category = parent.getCategory();
        this.visible = visible;
    }


    public String getName() {
        return this.name;
    }

    public Module getParent() {
        return this.parent;
    }

    public Type getType() {
        return this.type;
    }

    public Module.Category getCategory() {
        return this.category;
    }

    public T get() {
        return value;
    }

    public boolean get(String mode) {
        return value.equals(mode);
    }

    public void setValue(T value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    public int getInc() {
        return inc;
    }

    public List<String> getModes() {
        return modes;
    }

    public enum Type {
        Integer, Double, Boolean, Page, Mode,Description,String
    }
}
