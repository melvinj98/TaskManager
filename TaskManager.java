package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TaskManager implements java.io.Serializable {
    static Scanner keyboard = new Scanner(System.in);
    static Map<Integer, ArrayList<String>> toDo = new HashMap<>();
    static Map<Integer, ArrayList<String>> inProgress = new HashMap<>();
    static Map<Integer, ArrayList<String>> completed = new HashMap<>();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        readFromFile(toDo, "toDo");
        readFromFile(inProgress, "inProgress");
        readFromFile(completed, "completed");
        menu();
    }

    public static void menu() {
        do {
            System.out.println("TO DO");
            viewTasks(toDo);
            System.out.println("\n");
            System.out.println("IN PROGRESS");
            viewTasks(inProgress);
            System.out.println("\n");
            System.out.println("COMPLETED");
            viewTasks(completed);
            System.out.println("\n");

            System.out.println("--------------------------");
            System.out.println("What would you like to do?");
            System.out.println("1: Add task to To-Do List");
            System.out.println("2: Add task to In-Progress List");
            System.out.println("3: Add task to Completed List");
            System.out.println("4: Remove task from To-Do List");
            System.out.println("5: Remove task from In-Progress List");
            System.out.println("6: Move task");
            System.out.println("7: Quit");
            System.out.println("--------------------------");

            String option = keyboard.next();

            switch (option) {
                case "1" -> addTask(toDo, "toDo");
                case "2" -> addTask(inProgress, "inProgress");
                case "3" -> addTask(completed, "completed");
                case "4" -> removeTask(toDo);
                case "5" -> removeTask(inProgress);
                case "6" -> moveTask();
                case "7" -> System.exit(0);
                //case "7" -> getList(toDo);
            }
        } while (true);
    }

    public static void readFromFile(Map<Integer, ArrayList<String>> list, String listName) throws IOException, ClassNotFoundException {
        try {
            FileInputStream fis = new FileInputStream(listName + ".ser");
            ObjectInputStream ois = new ObjectInputStream(fis);

            Map<Integer, ArrayList<String>> mapInFile = (Map<Integer, ArrayList<String>>) ois.readObject();

            ois.close();
            fis.close();

            mapInFile.forEach((k, v) -> {
                if (v.get(1) != null) {
                    list.put(k, new ArrayList<String>());
                    list.get(k).add(v.get(0));
                    list.get(k).add(v.get(1));
                } else {
                    list.put(k, new ArrayList<String>());
                    list.get(k).add(v.get(0));
                }
            });
            System.out.println("loading from file completed");
        } catch (IOException e) {
            System.out.println(listName + ".ser not found... creating file");
            writeToFile(list, listName);
            readFromFile(list, listName);
        }
    }

    public static void writeToFile(Map<Integer, ArrayList<String>> list, String listName) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(listName + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(list);
            out.close();
            fos.close();
            System.out.println("serialization complete for " + listName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void moveTask() {
        System.out.println("--------------------------");
        System.out.println("Which list are you moving the task from?");
        System.out.println("1: To-Do List");
        System.out.println("2: In-Progress List");
        System.out.println("3: Completed List");
        System.out.println("--------------------------");
        int selectedList = keyboard.nextInt();

        if (selectedList == 1) {
            System.out.println("--------------------------");
            System.out.println("Which task would you like to move?");
            viewTasks(toDo);
            System.out.println("--------------------------");
            int selectedTask = keyboard.nextInt();

            System.out.println("--------------------------");
            System.out.println("Which list is this task being moved to?");
            System.out.println("1: In-Progress List");
            System.out.println("2: Completed List");
            System.out.println("--------------------------");
            int newList = keyboard.nextInt();

            if (newList == 1) {
                int taskNumber = inProgress.size() + 1;
                String task = toDo.get(selectedTask).get(0);
                String dueDate = toDo.get(selectedTask).get(1);

                inProgress.put(taskNumber, new ArrayList<String>());
                inProgress.get(taskNumber).add(task);
                inProgress.get(taskNumber).add(dueDate);
                toDo.remove(taskNumber);

            }
        }

    }

    //manually select task to remove

    public static void removeTask(Map<Integer, ArrayList<String>> list) {
        System.out.println("Which task would you like to remove?");
        viewTasks(list);
        int taskSelected = keyboard.nextInt();
        list.remove(taskSelected);
        viewTasks(list);
    }


    //prints out tasks to terminal

    public static void viewTasks(Map<Integer, ArrayList<String>> list) {
        list.forEach((k, v) -> {
            if (v.get(1).equals("")) {
                System.out.println(k + ": " + v.get(0));
            } else {
                System.out.println(k + ": " + v.get(0) + " due " + v.get(1));
            }
        });
    }


    //add task manually

    public static void addTask(Map<Integer, ArrayList<String>> list, String listName) {
        System.out.println("Enter task:");
        String task = keyboard.next();
        int taskNumber = list.size() + 1;

        list.put(taskNumber, new ArrayList<>());
        list.get(taskNumber).add(task);
        System.out.println("Is there a due date for this task?[Y/N]");
        String dueDateOption = keyboard.next();
        if (!dueDateOption.equals("")) {
            if (dueDateOption.equalsIgnoreCase("y")) {
                System.out.println("Enter due date:");
                String dueDate = keyboard.next();
                list.get(taskNumber).add(dueDate);
            } else
                list.get(taskNumber).add("");
        } else
            addTask(list, listName);

        try {
            writeToFile(list, listName);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static Map<Integer, ArrayList<String>> getToDoList() throws ClassNotFoundException, IOException{
        readFromFile(toDo,"toDo");
        return toDo;
    }

    public static Map<Integer, ArrayList<String>> getInProgressList() throws ClassNotFoundException, IOException {
        readFromFile(inProgress,"inProgress");
        return inProgress;
    }

    public static Map<Integer, ArrayList<String>> getCompletedList() throws ClassNotFoundException, IOException{
        readFromFile(completed,"completed");
        return completed;
    }
}