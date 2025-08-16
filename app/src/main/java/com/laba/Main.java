package com.laba;

import com.laba.dao.CatDao;
import com.laba.dao.OwnerDao;
import com.laba.impl.CatDaoImpl;
import com.laba.impl.OwnerDaoImpl;
import com.laba.entity.Cat;
import com.laba.entity.Color;
import com.laba.entity.Owner;
import com.laba.service.CatService;
import com.laba.service.OwnerService;
import com.laba.impl.CatServiceImpl;
import com.laba.impl.OwnerServiceImpl;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * CLI app Cat-Owner managing service.
 */
public class Main {

    private static CatService catService;
    private static OwnerService ownerService;
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        OwnerDao ownerDao = new OwnerDaoImpl(sessionFactory);
        CatDao catDao = new CatDaoImpl(sessionFactory);
        ownerService = new OwnerServiceImpl(ownerDao);
        catService = new CatServiceImpl(ownerDao, catDao);

        while (true) {
            System.out.println("1 Manage cats");
            System.out.println("2 Manage owners");
            System.out.println("0 Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": manageCats(); break;
                case "2": manageOwners(); break;
                case "0":
                    System.out.println("Exiting program");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Try again");
            }
        }
    }

    private static void manageCats() {
        while (true) {
            System.out.println("1 - Add cat");
            System.out.println("2 - Update cat");
            System.out.println("3 - Delete cat");
            System.out.println("4 - Find cat by id");
            System.out.println("5 - Show all cats");
            System.out.println("6 - Find cats by name");
            System.out.println("7 - Find cats by owner name");
            System.out.println("8 - Add friend to cat");
            System.out.println("9 - Remove friend from cat");
            System.out.println("10 - Show friends of cat");
            System.out.println("0 - Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": createCat(); break;
                case "2": updateCat(); break;
                case "3": deleteCat(); break;
                case "4": findCatById(); break;
                case "5": findAllCats(); break;
                case "6": findCatsByName(); break;
                case "7": findCatsByOwnerName(); break;
                case "8": addFriendToCat(); break;
                case "9": removeFriendFromCat(); break;
                case "10": showFriendsOfCat(); break;
                case "0": return;
                default: System.out.println("Try again"); break;
            }
        }
    }

    private static void manageOwners() {
        while (true) {
            System.out.println("1 - Add owner");
            System.out.println("2 - Update owner");
            System.out.println("3 - Delete owner");
            System.out.println("4 - Find owner by id");
            System.out.println("5 - Show all owners");
            System.out.println("6 - Find owners by name");
            System.out.println("7 - Find owners by cat name");
            System.out.println("8 - Find owner by cat id");
            System.out.println("9 - Show cats by owner id");
            System.out.println("0 - Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1": createOwner(); break;
                case "2": updateOwner(); break;
                case "3": deleteOwner(); break;
                case "4": findOwnerById(); break;
                case "5": findAllOwners(); break;
                case "6": findOwnersByName(); break;
                case "7": findOwnersByCatName(); break;
                case "8": findOwnerByCatId(); break;
                case "9": findCatsByOwnerId(); break;
                case "0": return;
                default: System.out.println("Try again"); break;
            }
        }
    }

    //object builders

    private static Owner buildOwnerFromInput() {
        System.out.print("Owner name: ");
        String name = scanner.nextLine();

        System.out.print("Birthday (YYYY-MM-DD): ");
        String birthday = scanner.nextLine();
        LocalDate parsedBirthday;
        try {
            parsedBirthday = LocalDate.parse(birthday);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
        }

        return new Owner.Builder()
                .name(name)
                .birthday(parsedBirthday)
                .build();
    }

    private static void updateOwnerFromInput(Owner owner) {
        System.out.print("New name (leave empty to skip): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) owner.setName(name);

        System.out.print("New birth birthday (YYYY-MM-DD) (leave empty to skip): ");
        String birthday = scanner.nextLine();
        if (!birthday.isBlank()) {
            try {
                owner.setBirthday(LocalDate.parse(birthday));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
            }
        }
    }


    private static Cat buildCatFromInput() {
        System.out.print("Cat name: ");
        String name = scanner.nextLine();

        System.out.print("Birthday (YYYY-MM-DD): ");
        LocalDate birthday = LocalDate.parse(scanner.nextLine());

        System.out.print("Breed: ");
        String breed = scanner.nextLine();

        System.out.print("Color (WHITE, BLACK, GINGER, GREY, BROWN, RAINBOW): ");
        Color color = Color.valueOf(scanner.nextLine().trim().toUpperCase());

        System.out.print("Owner id (leave blank to create new): ");
        String ownerIdStr = scanner.nextLine();

        Owner owner;
        if (ownerIdStr.isBlank()) {
            owner = buildOwnerFromInput();
            ownerService.saveOwner(owner);
            System.out.println("New owner successfully created and assigned");
        } else {
            Long ownerId = Long.parseLong(ownerIdStr);
            owner = ownerService.getOwnerById(ownerId);
            if (owner == null) {
                throw new IllegalArgumentException("Owner with this id not found");
            }
        }

        return new Cat.Builder()
                .name(name)
                .birthday(birthday)
                .breed(breed)
                .color(color)
                .owner(owner)
                .build();
    }

    private static void updateCatFromInput(Cat cat) {
        System.out.print("New name: ");
        String name = scanner.nextLine();
        if (!name.isBlank()) cat.setName(name);

        System.out.print("New birthday (YYYY-MM-DD): ");
        String birthday = scanner.nextLine();
        if (!birthday.isBlank()) {
            try {
                cat.setBirthday(LocalDate.parse(birthday));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
            }
        }

        System.out.print("New breed: ");
        String breed = scanner.nextLine();
        if (!breed.isBlank()) cat.setBreed(breed);

        System.out.print("New color: ");
        String color = scanner.nextLine();
        if (!color.isBlank()) {
            try {
                cat.setColor(Color.valueOf(color.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid color, allowed values: WHITE, BLACK, GINGER, GREY, BROWN, RAINBOW");
            }
        }

        System.out.print("New owner id (leave blank to skip): ");
        String ownerIdStr = scanner.nextLine();
        if (!ownerIdStr.isBlank()) {
            Long ownerId = Long.parseLong(ownerIdStr);
            Owner owner = ownerService.getOwnerById(ownerId);
            if (owner == null) {
                throw new IllegalArgumentException("Owner with this id not found");
            }
            cat.setOwner(owner);
        }
    }


    //Owner methods

    private static void createOwner() {
        try {
            Owner owner = buildOwnerFromInput();
            ownerService.saveOwner(owner);
            System.out.println("Owner successfully added");
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private static void updateOwner() {
        try {
            System.out.print("Owner id to update: ");
            Long id = Long.parseLong(scanner.nextLine());
            Owner owner = ownerService.getOwnerById(id);
            if (owner == null) {
                System.out.println("Owner with this id not found");
                return;
            }

            updateOwnerFromInput(owner);
            ownerService.updateOwner(owner);
            System.out.println("Owner successfully updated");
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private static void deleteOwner() {
        try {
            System.out.print("Owner id to delete: ");
            Long id = Long.parseLong(scanner.nextLine());
            Owner owner = ownerService.getOwnerById(id);
            if (owner == null) {
                System.out.println("Owner with this id not found");
                return;
            }
            ownerService.deleteOwner(owner);
            System.out.println("Owner successfully deleted");
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }


    //Cat methods

    private static void createCat() {
        try {
            Cat cat = buildCatFromInput();
            catService.saveCat(cat);
            System.out.println("Cat successfully added");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void updateCat() {
        try {
            System.out.print("Cat id to update: ");
            Long id = Long.parseLong(scanner.nextLine());
            Cat cat = catService.getCatById(id);
            if (cat == null) {
                System.out.println("Cat with this id not found");
                return;
            }

            updateCatFromInput(cat);
            catService.updateCat(cat);
            System.out.println("Cat successfully updated");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void deleteCat() {
        try {
            System.out.print("Cat id to delete: ");
            Long id = Long.parseLong(scanner.nextLine());
            Cat cat = catService.getCatById(id);
            if (cat == null) {
                System.out.println("Cat with this id not found");
                return;
            }
            catService.deleteCat(cat);
            System.out.println("Cat successfully deleted");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    //Friends methods

    private static void addFriendToCat() {
        try {
            System.out.print("Cat id: ");
            Long catId = Long.parseLong(scanner.nextLine());
            Cat cat = catService.getCatById(catId);
            if (cat == null) {
                System.out.println("Cat with this id not found");
                return;
            }

            System.out.print("Friend cat id (leave blank to create new): ");
            String friendIdStr = scanner.nextLine();
            Cat friend;
            if (friendIdStr.isBlank()) {
                friend = buildCatFromInput();
                catService.saveCat(friend);
                System.out.println("New cat created as a friend");
            } else {
                Long friendId = Long.parseLong(friendIdStr);
                friend = catService.getCatById(friendId);
                if (friend == null) {
                    System.out.println("Friend cat with this id not found");
                    return;
                }
            }

            catService.addFriend(cat.getId(), friend.getId());
            System.out.println("Friend successfully added");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void removeFriendFromCat() {
        try {
            System.out.print("Cat id: ");
            Long catId = Long.parseLong(scanner.nextLine());
            Cat cat = catService.getCatById(catId);
            if (cat == null) {
                System.out.println("Cat with this id not found");
                return;
            }

            System.out.print("Friend cat id to remove: ");
            Long friendId = Long.parseLong(scanner.nextLine());
            Cat friend = catService.getCatById(friendId);
            if (friend == null) {
                System.out.println("Friend cat with this id not found");
                return;
            }

            catService.removeFriend(cat.getId(), friend.getId());
            System.out.println("Friend successfully removed");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void showFriendsOfCat() {
        try {
            System.out.print("Cat id: ");
            Long catId = Long.parseLong(scanner.nextLine());
            Cat cat = catService.getCatById(catId);
            if (cat == null) {
                System.out.println("Cat with this id not found");
                return;
            }

            var friends = catService.getFriends(cat.getId());
            if (friends.isEmpty()) {
                System.out.println("This cat has no friends");
            } else {
                friends.forEach(Main::printCat);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    // find cat methods

    private static void findCatById() {
        try {
            System.out.print("Cat id to find: ");
            Long id = Long.parseLong(scanner.nextLine());
            Cat cat = catService.getCatById(id);
            if (cat == null) {
                System.out.println("Cat with this id not found");
            } else {
                printCat(cat);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void findAllCats() {
        try {
            List<Cat> cats = catService.getAllCats();
            if (cats.isEmpty()) {
                System.out.println("No cats found");
            } else {
                cats.forEach(Main::printCat);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void findCatsByName() {
        try {
            System.out.print("Cat name to find: ");
            String name = scanner.nextLine();
            List<Cat> cats = catService.findCatsByName(name);
            if (cats.isEmpty()) {
                System.out.println("No cats with this name found");
            } else {
                cats.forEach(Main::printCat);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private static void findCatsByOwnerName() {
        try {
            System.out.print("Owner name to find cats: ");
            String ownerName = scanner.nextLine();
            List<Cat> cats = catService.findCatsByOwnerName(ownerName);
            if (cats.isEmpty()) {
                System.out.println("No cats found for this owner");
            } else {
                cats.forEach(Main::printCat);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    //find owner methods

    private static void findOwnerById() {
        try {
            System.out.print("Owner id to find: ");
            Long id = Long.parseLong(scanner.nextLine());
            Owner owner = ownerService.getOwnerById(id);
            if (owner == null) {
                System.out.println("Owner with this id not found");
            } else {
                printOwner(owner);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private static void findAllOwners() {
        try {
            List<Owner> owners = ownerService.getAllOwners();
            if (owners.isEmpty()) {
                System.out.println("No owners found");
            } else {
                owners.forEach(Main::printOwner);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private static void findOwnersByName() {
        try {
            System.out.print("Owner name to find: ");
            String name = scanner.nextLine();
            List<Owner> owners = ownerService.findOwnersByName(name);
            if (owners.isEmpty()) {
                System.out.println("No owners with this name found");
            } else {
                owners.forEach(Main::printOwner);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private static void findOwnersByCatName() {
        try {
            System.out.print("Cat name to find owners: ");
            String catName = scanner.nextLine();
            List<Owner> owners = ownerService.findOwnersByCatName(catName);
            if (owners.isEmpty()) {
                System.out.println("No owners with this cat found");
            } else {
                owners.forEach(Main::printOwner);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private static void findOwnerByCatId() {
        try {
            System.out.print("Cat id to find owner: ");
            Long catId = Long.parseLong(scanner.nextLine());
            Owner owner = ownerService.findOwnerByCatId(catId);
            if (owner == null) {
                System.out.println("No owner found for this cat");
            } else {
                printOwner(owner);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private static void findCatsByOwnerId() {
        try {
            System.out.print("Owner id to find cats: ");
            Long ownerId = Long.parseLong(scanner.nextLine());
            List<Cat> cats = ownerService.getCatsByOwnerId(ownerId);
            if (cats.isEmpty()) {
                System.out.println("This owner has no cats");
            } else {
                cats.forEach(Main::printCat);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    //print methods

    private static void printCat(Cat cat) {
        System.out.printf("id: %d, Name: %s, Birthday: %s, Breed: %s, Color: %s, Owner: %s%n",
                cat.getId(),
                cat.getName(),
                cat.getBirthday(),
                cat.getBreed(),
                cat.getColor(),
                cat.getOwner() != null ? cat.getOwner().getName(): "Homeless");
    }

    private static void printOwner(Owner owner) {
        System.out.printf("id: %d, Name: %s, Birthday: %s, Number of cats: %d%n",
                owner.getId(),
                owner.getName(),
                owner.getBirthday(),
                owner.getCats() != null ? owner.getCats().size(): 0);
    }
}