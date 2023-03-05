

import java.util.*;

public class Group extends ArrayList<Student> {
    public Assistant assistant;
    public String ID;
    public Comparator<Student> comp;
    public Group(String ID, Assistant assistant) {
        this.ID = ID;
        this.assistant = assistant;
    }
    public Group(String ID, Assistant assistant, Comparator<Student> comp) {
        this.ID = ID;
        this.assistant = assistant;
        this.comp  = comp;
    }

    public Assistant getAssistant() {
        return assistant;
    }
    public void printGroup() {
      System.out.print("Grupa " + ID + " asistentul: " + assistant.toString() + " studentii: ");
      this.forEach(elem -> {
            System.out.print(elem.toString() + " ");
        });
        System.out.print("\n");

    }

}
