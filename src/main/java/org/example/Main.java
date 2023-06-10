package org.example;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        JsonCreator SSS = new JsonCreator();

        FileWork XXX = new FileWork();

        File file = new File("C:\\Users\\andrj\\IdeaProjects\\gusevs_tieto_task_\\src\\main\\java\\org\\example\\Input.txt");

        XXX.getInformationFromFile(file);

        SSS.getOutputFile(XXX.getTransactions());

    }

}
