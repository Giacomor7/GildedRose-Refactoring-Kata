package com.gildedrose

class AgedBrie(sellIn: Int, quality: Int, isConjured: Boolean) : ItemType("Aged Brie", sellIn, quality, isConjured) {
    override fun updateQuality() {
        this.sellIn -= 1

        if (this.quality < 50){
            this.quality += 1
        }

        if (this.sellIn < 0 && this.quality < 50){
            this.quality += 1
        }
    }
}