package hrytsenko.csv.mediator;

import hrytsenko.csv.core.Mediator;

import java.util.Collection;
import java.util.Collections;

public abstract class Accumulator implements Mediator {

    private String name;

    public Mediator into(String name) {
        this.name = name;
        return this;
    }

    public <T> T valueOf(String name) {
        if (name == null) {
            return null;
        }

        if (name.equals(this.name)) {
            @SuppressWarnings("unchecked")
            T value = (T) value();
            return value;
        }

        Collection<Mediator> children = descendants();
        for (Mediator child : children) {
            if (child instanceof Accumulator) {
                T value = ((Accumulator) child).valueOf(name);
                if (value != null) {
                    return value;
                }
            }
        }

        return null;
    }

    protected Object value() {
        return null;
    }

    protected Collection<Mediator> descendants() {
        return Collections.emptyList();
    }

}
