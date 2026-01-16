# Inventory API Reference

Complete reference for the HytaleLoader Inventory API.

## Overview

The Inventory API allows you to manage player inventories, including adding, removing, and modifying items.

## Classes

### Inventory (`fr.hytale.loader.api.inventory.Inventory`)
The main class for interacting with a player's inventory and inventories in general.

### Item (`fr.hytale.loader.api.Item`)
A wrapper around Hytale's item stack.

## Usage

### Getting an Inventory
```java
Inventory inv = player.getInventory();
```

### Creating Items
```java
// Create an item by ID and quantity
Item sword = new Item("Weapon_Sword_Cobalt", 1);
Item apples = new Item("Consumable_Apple", 5);
```

### Adding Items
```java
// Adds an item to the first available slot in storage
inv.addItem(sword);
```

### Setting Items in Specific Slots
```java
// Set item in hotbar slot 0 (first slot)
inv.setItem(apples, 0);

// Set item in storage slot 13
inv.setItem(sword, 13);
```
- **Slots 0-8**: Hotbar
- **Slots 9+**: Main Storage

### Getting Items
```java
// Get item at specific slot
Item item = inv.getItem(0);

// Get all items
List<Item> allItems = inv.getItems();
for (Item i : allItems) {
    System.out.println(i.getId() + " x" + i.getQuantity());
}
```

### Clearing Inventory
```java
inv.clear();
```
Removes all items from the inventory (Hotbar, Storage, Armor).

## Item Properties

### getId()
Returns the item ID (e.g., "hytale:sword").

### getQuantity()
Returns the number of items in the stack.

### isBroken()
Checks if the item is broken.

### maxDurability()
Returns the maximum durability of the item.

### getBlockKey()
Returns the block key associated with the item (if applicable).

### isEquivalentType(Item other)
Checks if two items are of the same type (ignoring quantity/metadata).

## Example

```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    Inventory inv = event.getPlayer().getInventory();
    
    if (inv != null) {
        inv.clear();
        
        // Give starter kit
        inv.setItem(new Item("Weapon_Sword_Iron", 1), 0);
        inv.setItem(new Item("Tool_Pickaxe_Iron", 1), 1);
        inv.addItem(new Item("Consumable_Bread", 16));
        
        event.getPlayer().sendMessage("Starter kit received!");
    }
}
```
