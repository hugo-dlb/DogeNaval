package com.daltrisseville.DogeNaval.Users;

import java.io.*;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserHandler {

    private List<User> usersList;
    private String filepath = "/save/test.json";

    public List<User> getUsersList() {
        return usersList;
    }

    public UserHandler() {
        usersList = new List<User>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<User> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(User user) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends User> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, Collection<? extends User> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public User get(int index) {
                return null;
            }

            @Override
            public User set(int index, User element) {
                return null;
            }

            @Override
            public void add(int index, User element) {

            }

            @Override
            public User remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<User> listIterator() {
                return null;
            }

            @Override
            public ListIterator<User> listIterator(int index) {
                return null;
            }

            @Override
            public List<User> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
        //peuplement de la liste
        Gson gson = new Gson();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
            usersList = gson.fromJson(bufferedReader, usersList.getClass());
            int UserMaxId = 0;
            for(Iterator<User> i = usersList.iterator(); i.hasNext();) {
                User currentUser = i.next();
                if (currentUser.getId() > UserMaxId) {
                    UserMaxId = currentUser.getId();
                }
            }
            User.setMaxId(UserMaxId);
        }
        catch (FileNotFoundException Ex) {
            //alors le fichier de sauvegarde n'existe pas, rien à faire ici
        }
    }

    public boolean checkUserAuthentication(String userNameToTest, String passwordToTest)
    {
        for(Iterator<User> i = usersList.iterator(); i.hasNext();) {
            User currentUser = i.next();
            if (currentUser.getUsername() == userNameToTest
                && currentUser.getPassword() == passwordToTest) {
                return true;
            }
        }
        return false;
    }

    public void createUser(String newUsername, String newPassword) {
        User newUser = new User(newUsername, newPassword);
        usersList.add(newUser);
    }

    public boolean saveUsersList() {
        try {
            Writer writer = new FileWriter(filepath);
            Gson gson = new GsonBuilder().create();
            gson.toJson(usersList, writer);
            return true; //ça a marché!
        }
        catch (IOException Ex) {
            return false; //oups, bug
        }
    }
}
