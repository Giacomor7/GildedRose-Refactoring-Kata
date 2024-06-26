package com.gildedrose

open class ItemType(name: String, sellIn: Int, quality: Int, var isConjured: Boolean) : Item(name, sellIn, quality) {
    open fun updateQuality() {

        this.sellIn -= 1

        val multiplier = (if (this.isConjured) 2 else 1) * (if (this.sellIn < 0) 2 else 1)

        this.quality -= multiplier

        if (this.quality < 0){
            this.quality = 0
        }
    }
}