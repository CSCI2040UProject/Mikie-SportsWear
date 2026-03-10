package nullscape.mike.model;

import java.util.Arrays;

//Product_ID,Name,Description,Categories,Price,Color,Other_Colors,Product_URL,Image_URLs
public class Item {
    private String id;
    private String name;
    private String description;
    private String[] categories;
    private String price;
    private String color;
    private String[] otherColors;
    private String productUrl;
    private String thumbnailUrl;
    private String[] images;

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + id + '\'' +
                ", itemName='" + name + '\'' +
                ", itemDescription='" + description + '\'' +
                ", itemCategories=" + Arrays.toString(categories) +
                ", itemPrice='" + price + '\'' +
                ", itemColor='" + color + '\'' +
                ", otherColors=" + Arrays.toString(otherColors) +
                ", productUrl='" + productUrl + '\'' +
                ", itemImages=" + Arrays.toString(images) +
                '}';
    }

    public Item() {
    }

    public Item(String id, String name, String description, String[] categories, String price, String color, String[] otherColors, String productUrl, String thumbnailUrl, String[] images) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.price = price;
        this.color = color;
        this.otherColors = otherColors;
        this.productUrl = productUrl;
        this.images = images;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getCategories() {
        return categories;
    }
    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }

    public String[] getOtherColors() {
        return otherColors;
    }
    public void setOtherColors(String[] otherColors) {
        this.otherColors = otherColors;
    }

    public String getProductUrl() {
        return productUrl;
    }
    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String[] getImages() {
        return images;
    }
    public void setImages(String[] images) {
        this.images = images;
    }
}
