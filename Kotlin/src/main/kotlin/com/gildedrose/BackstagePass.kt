package com.gildedrose

class BackstagePass(sellIn: Int, quality: Int, isConjured: Boolean) : ItemType("Backstage passes to a TAFKAL80ETC concert", sellIn, quality, isConjured) {
    override fun updateQuality() {
        if (this.quality < 50){
            this.quality += 1
        }

        if (this.sellIn < 11 && this.quality < 50){
            this.quality += 1
        }

        if (this.sellIn < 6 && this.quality < 50){
            this.quality += 1
        }



        this.sellIn -= 1

        if (this.sellIn < 0){
            this.quality = 0
        }
    }
}