package hrytsenko.csv.mediator;

import hrytsenko.csv.Mediator;

import java.util.Collection;
import java.util.Collections;

public abstract class Accumulator implements Mediator {

    private String name;

    public final Mediator into(String name) {
        this.name = name;
        return this;
    }

    public final Object valueOf(String name) {
        if (name == null) {
            return null;
        }

        if (name.equals(this.name)) {
            return value();
        }

        Collection<Mediator> children = children();
        for (Mediator child : children) {
            if (child instanceof Accumulator) {
                Object value = ((Accumulator) child).valueOf(name);
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

    protected Collection<Mediator> children() {
        return Collections.emptyList();
    }

}
