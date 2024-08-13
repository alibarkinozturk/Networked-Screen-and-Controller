/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package warnings;

import java.util.Objects;

/**
 *
 * @author aliba
 */
public class Warning {

    int id = 0;
    int priority;
    String message;

    public Warning(int id, int priority, String message) {
        this.id = id;
        this.priority = priority;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
public boolean equals(Object obj) {
    if (this == obj) {
        return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
        return false;
    }
    Warning warning = (Warning) obj;
    return message.equals(warning.message);
}

@Override
public int hashCode() {
    return Objects.hash(message);
}


}
