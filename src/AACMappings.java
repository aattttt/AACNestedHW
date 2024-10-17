
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KeyNotFoundException;
import edu.grinnell.csc207.util.NullKeyException;

public class AACMappings implements AACPage {

    /*
	 * Fields
	 * 
     */
    AssociativeArray<String, AACCategory> categoryTracker = new AssociativeArray<String, AACCategory>();
    AACCategory homepage = new AACCategory("");
    String selectedCat = "";

    /**
     * Creates a set of mappings for the AAC based on the provided file. The
     * file is read in to create categories and fill each of the categories with
     * initial items. The file is formatted as the text location of the category
     * followed by the text name of the category and then one line per item in
     * the category that starts with > and then has the file name and text of
     * that image
     *
     * for instance: img/food/plate.png food
     * >img/food/icons8-french-fries-96.png french fries
     * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png
     * clothing >img/clothing/collaredshirt.png collared shirt
     *
     * represents the file with two categories, food and clothing and food has
     * french fries and watermelon and clothing has a collared shirt
     *
     * @param filename the name of the file that stores the mapping information
     */
    public AACMappings(String filename) {
        try {
            File input = new File(filename);
            Scanner eyes = new Scanner(input);
            AACCategory current = null;
            while (eyes.hasNextLine()) {
                String[] commands = eyes.nextLine().split(" ");
                String location = commands[0];
                String title = commands[1];
                for (int i = 2; i < commands.length; i++) { // set the title
                    title += " " + commands[i];
                } // for
                if (location.charAt(0) != '>') { // New catagory case
                    current = new AACCategory(title);
                    try {
                        categoryTracker.set(title, current);
                    } catch (NullKeyException ex) {
                    }
                    homepage.addItem(location, title);
                } else { // same catagory case
                    current.addItem(location.substring(1, location.length()), title);
                } // if else
            } // while
            eyes.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        } // catch
    } // method

    /*
     * Given the image location selected, it determines the action to be taken.
     * This can be updating the information that should be displayed or
     * returning text to be spoken. If the image provided is a category, it
     * updates the AAC's current category to be the category associated with
     * that image and returns the empty string. If the AAC is currently in a
     * category and the image provided is in that category, it returns the text
     * to be spoken.
     *
     * @param imageLoc the location where the image is stored
     * @return if there is text to be spoken, it returns that information,
     * otherwise it returns the empty string
     * @throws NoSuchElementException if the image provided is not in the
     * current category
     */
    public String select(String imageLoc) {
        try {
            if (categoryTracker.get(selectedCat).hasImage(imageLoc)) {
                return categoryTracker.get(selectedCat).select(imageLoc);

            } else {
                selectedCat = homepage.select(imageLoc);
                return "";
            }
        } catch (KeyNotFoundException ex) {
            return null;
        }
    }

    /**
     * Provides an array of all the images in the current category
     *
     * @return the array of images in the current category; if there are no
     * images, it should return an empty array
     */
    public String[] getImageLocs() {
        if (selectedCat.isEmpty()) {
            return homepage.getImageLocs();
        }
        try {
            return categoryTracker.get(selectedCat).getImageLocs();
        } catch (KeyNotFoundException e) {
            return null;
        }
    }

    /**
     * Resets the current category of the AAC back to the default category
     */
    public void reset() {
        selectedCat = "";
    }

    /**
     * Writes the ACC mappings stored to a file. The file is formatted as the
     * text location of the category followed by the text name of the category
     * and then one line per item in the category that starts with > and then
     * has the file name and text of that image
     *
     * for instance: img/food/plate.png food
     * >img/food/icons8-french-fries-96.png french fries
     * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png
     * clothing >img/clothing/collaredshirt.png collared shirt
     *
     * represents the file with two categories, food and clothing and food has
     * french fries and watermelon and clothing has a collared shirt
     *
     * @param filename the name of the file to write the AAC mapping to
     */
    public void writeToFile(String filename) {
        FileWriter file = null;
        try {
            file = new FileWriter(filename);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
        for (int i = 0; i < homepage.getImageLocs().length; i++) {
            try {
                String imageLoc = homepage.getImageLocs()[i];
                String curCategory = homepage.select(imageLoc);
                
                file.write(imageLoc + " " + curCategory + "\n");
                
                try {
                    AACCategory category = categoryTracker.get(curCategory);
                    String[] categoryItems = category.getImageLocs();
                    
                    for (int j = 0; j < categoryItems.length; j++) {
                        String itemImageLoc = categoryItems[j];
                        String itemText = category.select(itemImageLoc);
                        
                        file.write(">" + itemImageLoc + " " + itemText + "\n");
                    }
                } catch (KeyNotFoundException e) {
                }

            } catch (IOException ex) {
            }

        }
        try {
            file.close();
        } catch (IOException ex) {
        }
    }

    /**
     * Adds the mapping to the current category (or the default category if that
     * is the current category)
     *
     * @param imageLoc the location of the image
     * @param text the text associated with the image
     */
    public void addItem(String imageLoc, String text) {
        if (selectedCat.isEmpty()) {
            homepage.addItem(imageLoc, text);
        } else { 
            try {
                categoryTracker.get(selectedCat).addItem(imageLoc, text);
            } catch (KeyNotFoundException e) {
            }
        }
    }

    /**
     * Gets the name of the current category
     *
     * @return returns the current category or the empty string if on the
     * default category
     */
    public String getCategory() {
        return selectedCat;
    }

    /**
     * Determines if the provided image is in the set of images that can be
     * displayed and false otherwise
     *
     * @param imageLoc the location of the category
     * @return true if it is in the set of images that can be displayed, false
     * otherwise
     */
    public boolean hasImage(String imageLoc) {
        if (selectedCat.isEmpty()) {
            return homepage.hasImage(imageLoc);
        }
        try {
            return categoryTracker.get(selectedCat).hasImage(imageLoc);
        } catch (KeyNotFoundException e) {
            return false;
        }
    }
}
