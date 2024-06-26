package com.gildedrose

class GildedRose(var items: List<ItemType>) {

    init{
        val itemTypes = mutableListOf<ItemType>()
        for (item in items){
            when (item.name){
                "Aged Brie" -> {itemTypes += AgedBrie(item.sellIn, item.quality, item.isConjured)}
                "Sulfuras, Hand of Ragnaros" -> {itemTypes += Sulfuras()}
                "Backstage passes to a TAFKAL80ETC concert" -> {itemTypes += BackstagePass(item.sellIn, item.quality, item.isConjured)}
                else -> {itemTypes += item}
            }
        }
        items = itemTypes
    }

    fun updateQuality() {
        for (i in items.indices) {
            items[i].updateQuality()
        }
    }

}

