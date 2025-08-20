package com.laba;

import com.laba.dto.CatDto;
import com.laba.dto.OwnerDto;
import com.laba.entity.Cat;
import com.laba.entity.Color;
import com.laba.entity.Owner;
import com.laba.service.CatService;
import com.laba.service.OwnerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * The type Console.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class Console implements CommandLineRunner {

    private final CatService catService;
    private final OwnerService ownerService;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Instantiates a new Console.
     *
     * @param catService   the cat service
     * @param ownerService the owner service
     */
    public Console(CatService catService, OwnerService ownerService) {
        this.catService = catService;
        this.ownerService = ownerService;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        new SpringApplicationBuilder(Console.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Override
    public void run(String... args) {
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

    private void manageCats() {
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
            System.out.println("10 - Find cats by owner id");
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
                case "10": findCatsByOwnerId(); break;
                case "0": return;
                default: System.out.println("Try again"); break;
            }
        }
    }

    private void manageOwners() {
        while (true) {
            System.out.println("1 - Add owner");
            System.out.println("2 - Update owner");
            System.out.println("3 - Delete owner");
            System.out.println("4 - Find owner by id");
            System.out.println("5 - Show all owners");
            System.out.println("6 - Find owners by name");
            System.out.println("7 - Find owners by cat name");
            System.out.println("8 - Find owner by cat id");
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
                case "0": return;
                default: System.out.println("Try again"); break;
            }
        }
    }

    //object builders

    private OwnerDto buildOwnerFromInput() {
        System.out.print("Owner name: ");
        String name = scanner.nextLine();

        System.out.print("Birthday (YYYY-MM-DD): ");
        String birthdayStr = scanner.nextLine();

        LocalDate birthday;
        try {
            birthday = LocalDate.parse(birthdayStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setName(name);
        ownerDto.setBirthday(birthday);
        return ownerDto;
    }

    private void updateOwnerFromInput(OwnerDto ownerDto) {
        System.out.print("New name (leave empty to skip): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) ownerDto.setName(name);

        System.out.print("New birthday (YYYY-MM-DD) (leave empty to skip): ");
        String birthday = scanner.nextLine();
        if (!birthday.isBlank()) {
            try {
                ownerDto.setBirthday(LocalDate.parse(birthday));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
            }
        }
    }


    private CatDto buildCatFromInput() {
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

        OwnerDto ownerDto;
        if (ownerIdStr.isBlank()) {
            ownerDto = buildOwnerFromInput();
            ownerService.saveOwner(ownerDto);
            System.out.println("New owner successfully created and assigned");
        } else {
            Long ownerId = Long.parseLong(ownerIdStr);
            Owner existingOwner = ownerService.getOwnerById(ownerId).toEntity();
            if (existingOwner == null) {
                throw new IllegalArgumentException("Owner with this id not found");
            }
            ownerDto = OwnerDto.fromEntity(existingOwner);
        }

        CatDto catDto = new CatDto();
        catDto.setName(name);
        catDto.setBirthday(birthday);
        catDto.setBreed(breed);
        catDto.setColor(color);
        catDto.setOwner(ownerDto);
        catDto.setOwnerId(ownerDto.getId());
        return catDto;
    }

    private void updateCatFromInput(CatDto catDto) {
        System.out.print("New name (leave empty to skip): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) catDto.setName(name);

        System.out.print("New birthday (YYYY-MM-DD) (leave empty to skip): ");
        String birthday = scanner.nextLine();
        if (!birthday.isBlank()) {
            try {
                catDto.setBirthday(LocalDate.parse(birthday));
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
            }
        }

        System.out.print("New breed (leave empty to skip): ");
        String breed = scanner.nextLine();
        if (!breed.isBlank()) catDto.setBreed(breed);

        System.out.print("New color (leave empty to skip): ");
        String color = scanner.nextLine();
        if (!color.isBlank()) {
            try {
                catDto.setColor(Color.valueOf(color.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid color, allowed values: WHITE, BLACK, GINGER, GREY, BROWN, RAINBOW");
            }
        }

        System.out.print("New owner id (leave blank to skip): ");
        String ownerIdStr = scanner.nextLine();
        if (!ownerIdStr.isBlank()) {
            Long ownerId = Long.parseLong(ownerIdStr);
            OwnerDto ownerDto = ownerService.getOwnerById(ownerId);
            if (ownerDto.toEntity() == null) {
                throw new IllegalArgumentException("Owner with this id not found");
            }
            catDto.setOwner(ownerDto);
            catDto.setOwnerId(ownerDto.getId());
        }
    }

    //Owner methods

    private void createOwner() {
        try {
            OwnerDto ownerDto = buildOwnerFromInput();
            ownerService.saveOwner(ownerDto);
            System.out.println("Owner successfully added");
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private void updateOwner() {
        try {
            System.out.print("Owner id to update: ");
            Long id = Long.parseLong(scanner.nextLine());
            OwnerDto ownerDto = ownerService.getOwnerById(id);
            if (ownerDto == null) {
                System.out.println("Owner with this id not found");
                return;
            }

            updateOwnerFromInput(ownerDto);
            ownerService.updateOwner(ownerDto);
            System.out.println("Owner successfully updated");
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private  void deleteOwner() {
        try {
            System.out.print("Owner id to delete: ");
            Long id = Long.parseLong(scanner.nextLine());
            OwnerDto ownerDto = ownerService.getOwnerById(id);
            if (ownerDto == null) {
                System.out.println("Owner with this id not found");
                return;
            }
            ownerService.deleteOwner(ownerDto.getId());
            System.out.println("Owner successfully deleted");
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }


    //Cat methods

    private void createCat() {
        try {
            CatDto catDto = buildCatFromInput();
            catService.saveCat(catDto);
            System.out.println("Cat successfully added");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void updateCat() {
        try {
            System.out.print("Cat id to update: ");
            Long id = Long.parseLong(scanner.nextLine());
            CatDto catDto = catService.getCatById(id);
            if (catDto == null) {
                System.out.println("Cat with this id not found");
                return;
            }

            updateCatFromInput(catDto);
            catService.updateCat(catDto);
            System.out.println("Cat successfully updated");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void deleteCat() {
        try {
            System.out.print("Cat id to delete: ");
            Long id = Long.parseLong(scanner.nextLine());
            CatDto catDto = catService.getCatById(id);
            if (catDto == null) {
                System.out.println("Cat with this id not found");
                return;
            }
            catService.deleteCat(catDto.getId());
            System.out.println("Cat successfully deleted");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


    //Friends methods

    private void addFriendToCat() {
        try {
            System.out.print("Cat id: ");
            Long catId = Long.parseLong(scanner.nextLine());
            CatDto catDto = catService.getCatById(catId);
            if (catDto == null) {
                System.out.println("Cat with this id not found");
                return;
            }

            System.out.print("Friend cat id (leave blank to create new): ");
            String friendIdStr = scanner.nextLine();
            CatDto friend;
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

            catService.addFriend(catDto.getId(), friend.getId());
            System.out.println("Friend successfully added");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void removeFriendFromCat() {
        try {
            System.out.print("Cat id: ");
            Long catId = Long.parseLong(scanner.nextLine());
            CatDto catDto = catService.getCatById(catId);
            if (catDto == null) {
                System.out.println("Cat with this id not found");
                return;
            }

            System.out.print("Friend cat id to remove: ");
            Long friendId = Long.parseLong(scanner.nextLine());
            CatDto friend = catService.getCatById(friendId);
            if (friend == null) {
                System.out.println("Friend cat with this id not found");
                return;
            }

            catService.removeFriend(catDto.getId(), friend.getId());
            System.out.println("Friend successfully removed");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // find cat methods

    private void findCatById() {
        try {
            System.out.print("Cat id to find: ");
            Long id = Long.parseLong(scanner.nextLine());
            CatDto catDto = catService.getCatById(id);
            if (catDto == null) {
                System.out.println("Cat with this id not found");
            } else {
                printCat(catDto);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void findAllCats() {
        try {
            List<CatDto> cats = catService.getAllCats();
            if (cats.isEmpty()) {
                System.out.println("No cats found");
            } else {
                cats.forEach(Console::printCat);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void findCatsByName() {
        try {
            System.out.print("Cat name to find: ");
            String name = scanner.nextLine();
            List<CatDto> cats = catService.findCatsByName(name);
            if (cats.isEmpty()) {
                System.out.println("No cats with this name found");
            } else {
                cats.forEach(Console::printCat);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private void findCatsByOwnerName() {
        try {
            System.out.print("Owner name to find cats: ");
            String ownerName = scanner.nextLine();
            List<CatDto> cats = catService.findCatsByOwnerName(ownerName);
            if (cats.isEmpty()) {
                System.out.println("No cats found for this owner");
            } else {
                cats.forEach(Console::printCat);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    //find owner methods

    private void findOwnerById() {
        try {
            System.out.print("Owner id to find: ");
            Long id = Long.parseLong(scanner.nextLine());
            OwnerDto ownerDto = ownerService.getOwnerById(id);
            if (ownerDto == null) {
                System.out.println("Owner with this id not found");
            } else {
                printOwner(ownerDto);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private void findAllOwners() {
        try {
            List<OwnerDto> owners = ownerService.getAllOwners();
            if (owners.isEmpty()) {
                System.out.println("No owners found");
            } else {
                owners.forEach(Console::printOwner);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private void findOwnersByName() {
        try {
            System.out.print("Owner name to find: ");
            String name = scanner.nextLine();
            List<OwnerDto> owners = ownerService.findOwnersByName(name);
            if (owners.isEmpty()) {
                System.out.println("No owners with this name found");
            } else {
                owners.forEach(Console::printOwner);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private void findOwnersByCatName() {
        try {
            System.out.print("Cat name to find owners: ");
            String catName = scanner.nextLine();
            List<OwnerDto> owners = ownerService.findOwnersByCatName(catName);
            if (owners.isEmpty()) {
                System.out.println("No owners with this cat found");
            } else {
                owners.forEach(Console::printOwner);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private void findOwnerByCatId() {
        try {
            System.out.print("Cat id to find owner: ");
            Long catId = Long.parseLong(scanner.nextLine());
            OwnerDto ownerDto = ownerService.findOwnerByCatId(catId);
            if (ownerDto == null) {
                System.out.println("No owner found for this cat");
            } else {
                printOwner(ownerDto);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    private void findCatsByOwnerId() {
        try {
            System.out.print("Owner id to find cats: ");
            Long ownerId = Long.parseLong(scanner.nextLine());
            List<CatDto> cats = catService.findCatsByOwnerId(ownerId);
            if (cats.isEmpty()) {
                System.out.println("This owner has no cats");
            } else {
                cats.forEach(Console::printCat);
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }

    //print methods

    private static void printCat(CatDto cat) {
        System.out.printf("id: %d, Name: %s, Birthday: %s, Breed: %s, Color: %s, Owner: %s%n",
                cat.getId(),
                cat.getName(),
                cat.getBirthday(),
                cat.getBreed(),
                cat.getColor(),
                cat.getOwner() != null ? cat.getOwner().getName(): "Homeless");
    }

    private static void printOwner(OwnerDto owner) {
        System.out.printf("id: %d, Name: %s, Birthday: %s, Number of cats: %d%n",
                owner.getId(),
                owner.getName(),
                owner.getBirthday(),
                owner.getCatIds() != null ? owner.getCatIds().size(): 0);
    }
}