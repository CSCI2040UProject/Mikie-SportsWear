package nullscape.mike.model;

import java.util.Arrays;

//Product_ID,Name,Description,Categories,Price,Color,Other_Colors,Product_URL,Image_URLs
public class Item {
    private String itemId;
    private String itemName;
    private String itemDescription;
    private String[] itemCategories;
    private String itemPrice;
    private String itemColor;
    private String[] otherColors;
    private String productUrl;
    private String[] itemImages;

    @Override
    public String toString() {
        return "Item{" +
                "itemId='" + itemId + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemCategories=" + Arrays.toString(itemCategories) +
                ", itemPrice='" + itemPrice + '\'' +
                ", itemColor='" + itemColor + '\'' +
                ", otherColors=" + Arrays.toString(otherColors) +
                ", productUrl='" + productUrl + '\'' +
                ", itemImages=" + Arrays.toString(itemImages) +
                '}';
    }

    public Item() {
    }

    public Item(String itemId, String itemName, String itemDescription, String[] itemCategories, String itemPrice, String itemColor, String[] otherColors, String productUrl, String[] itemImages) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemCategories = itemCategories;
        this.itemPrice = itemPrice;
        this.itemColor = itemColor;
        this.otherColors = otherColors;
        this.productUrl = productUrl;
        this.itemImages = itemImages;
    }


    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String[] getItemCategories() {
        return itemCategories;
    }
    public void setItemCategories(String[] itemCategories) {
        this.itemCategories = itemCategories;
    }

    public String getItemPrice() {
        return itemPrice;
    }
    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemColor() {
        return itemColor;
    }
    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
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

    public String[] getItemImages() {
        return itemImages;
    }
    public void setItemImages(String[] itemImages) {
        this.itemImages = itemImages;
    }
}
