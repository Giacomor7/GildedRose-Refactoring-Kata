package com.gildedrose

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GildedRoseTest {

    private val repeatNumber = 100

    @Test
    fun checkTimePasses() {
        val items = listOf(ItemType("BasicItem", 5, 25, false)) // name, sellIn, quality
        val app = GildedRose(items)

        app.updateQuality()

        assertEquals(app.items[0].sellIn, 4)
        assert(app.items[0].quality < 25)
    }

    @Test
    fun checkQualityDegradesTwiceAsFastAfterSellByDate() {
        val items = listOf(ItemType("Item", 3, 20, false)) // name, sellIn, quality
        val app = GildedRose(items)

        app.updateQuality()

        val degradeRate = 20 - app.items[0].quality

        while (app.items[0].sellIn >= 0){
            app.updateQuality() // ensure we pass sell-by-date
        }

        val currentQuality = app.items[0].quality
        app.updateQuality()
        val updatedQuality = app.items[0].quality
        val newDegradeRate = currentQuality - updatedQuality

        assertEquals(newDegradeRate, 2 * degradeRate)
    }

    @Test
    fun checkQualityIsPositive() { // worth implementing this test for more items?
        val items = listOf(ItemType("new item", 4, 21, false)) // name, sellIn, quality
        val app = GildedRose(items)

        repeat(repeatNumber) {
            app.updateQuality()
            assert(app.items[0].quality >= 0)
        }
    }

    @Test
    fun checkAgedBrieIncreasesInQuality() {
        val items = listOf(ItemType("Aged Brie", 6, 22, false)) // name, sellIn, quality
        val app = GildedRose(items)

        app.updateQuality()

        assert(app.items[0].quality > 22)
    }

    @Test
    fun checkQualityIsUnder50() {
        val items = listOf(ItemType("newitem", 7, 23, false)) // name, sellIn, quality
        val app = GildedRose(items)

        repeat(repeatNumber) {
            app.updateQuality()
            assert(app.items[0].quality < 50)
        }
    }

    @Test
    fun checkSulfurasNeverHasToBeSold() {
        val items = listOf(ItemType("Sulfuras, Hand of Ragnaros", 0, 80, false)) // name, sellIn, quality
        val app = GildedRose(items)

        repeat(repeatNumber) {
            app.updateQuality()
            assertEquals(app.items[0].sellIn, 0)
        }
    }

    @Test
    fun checkSulfurasNeverDecreasesQuality() {
        val items = listOf(ItemType("Sulfuras, Hand of Ragnaros", 0, 80, false)) // name, sellIn, quality
        val app = GildedRose(items)

        repeat(repeatNumber) {
            app.updateQuality()
            assertEquals(app.items[0].quality, 80)
        }
    }

    @Test
    fun checkBackstagePassIncreaseInQualityBeforeConcert() {
        val items = listOf(ItemType("Backstage passes to a TAFKAL80ETC concert", 12, 5, false)) // name, sellIn, quality
        val app = GildedRose(items)

        var currentQuality = app.items[0].quality

        repeat(repeatNumber) {
            app.updateQuality()
            if (app.items[0].sellIn >= 0){ // sellIn = 0 is day of the concert so can still sell
                assert(app.items[0].quality > currentQuality)
                currentQuality = app.items[0].quality
            }
        }
    }

    @Test
    fun checkBackstagePassQualityIncreasesBy2With5to10DaysToGo() {
        val items = listOf(ItemType("Backstage passes to a TAFKAL80ETC concert", 12, 5, false)) // name, sellIn, quality
        val app = GildedRose(items)

        var currentQuality = app.items[0].quality

        repeat(repeatNumber) {
            app.updateQuality()
            if (app.items[0].sellIn in 6..10){
                assertEquals(app.items[0].quality, currentQuality + 2)
                currentQuality = app.items[0].quality
            }
        }
    }

    @Test
    fun checkBackstagePassQualityIncreasesBy3With5DaysToGo() {
        val items = listOf(ItemType("Backstage passes to a TAFKAL80ETC concert", 12, 5, false)) // name, sellIn, quality
        val app = GildedRose(items)

        repeat(repeatNumber) {
            val currentQuality = app.items[0].quality
            app.updateQuality()
            if (app.items[0].sellIn in 1..4){ // if sellIn < 5, then was < 6 before the update
                assertEquals(app.items[0].quality, currentQuality + 3)
            }

        }
    }

    @Test
    fun checkBackStagePassQualityDropsTo0AfterConcert(){
        val items = listOf(ItemType("Backstage passes to a TAFKAL80ETC concert", 12, 5, false)) // name, sellIn, quality
        val app = GildedRose(items)

        repeat(repeatNumber) {
            app.updateQuality()
            if (app.items[0].sellIn < 0){ // sellIn = 0 is day of the concert so can still sell
                assertEquals(app.items[0].quality, 0)
            }
        }
    }

    @Test
    fun checkConjuredItemQualityDegradesTwiceAsFast() {
        val items = listOf(
            ItemType("Basic", 10, 10, false),
            ItemType("BasicPastSellBy", -1, 10, false),
            ItemType("Aged Brie", 10, 10, false),
            ItemType("Sulfuras, Hand of Ragnaros", 0, 80, false),
            ItemType("Backstage passes to a TAFKAL80ETC concert", 12, 10, false),
            ItemType("Backstage passes to a TAFKAL80ETC concert", 8, 10, false),
            ItemType("Backstage passes to a TAFKAL80ETC concert", 2, 10, false),
            ItemType("Backstage passes to a TAFKAL80ETC concert", -2, 0, false))

        val conjuredItems = listOf(
            ItemType("Basic", 10, 10, true),
            ItemType("BasicPastSellBy", -1, 10, true),
            ItemType("Aged Brie", 10, 10, true),
            ItemType("Sulfuras, Hand of Ragnaros", 0, 80, true),
            ItemType("Backstage passes to a TAFKAL80ETC concert", 12, 10, true),
            ItemType("Backstage passes to a TAFKAL80ETC concert", 8, 10, true),
            ItemType("Backstage passes to a TAFKAL80ETC concert", 2, 10, true),
            ItemType("Backstage passes to a TAFKAL80ETC concert", -2, 0, true))

        val app = GildedRose(items)
        val conjuredApp = GildedRose(conjuredItems)

        app.updateQuality()
        conjuredApp.updateQuality()

        val degradeRates = LinkedHashMap<String, Int>()
        degradeRates["Basic"] = 10 - app.items[0].quality
        degradeRates["BasicPastSellBy"] = 10 - app.items[1].quality
        degradeRates["Aged Brie"] = 10 - app.items[2].quality
        degradeRates["Sulfuras"] = 80 - app.items[3].quality
        degradeRates["Backstage12Days"] = 10 - app.items[4].quality
        degradeRates["Backstage8Days"] = 10 - app.items[5].quality
        degradeRates["Backstage2Days"] = 10 - app.items[6].quality
        degradeRates["BackstageAfterConcert"] = 0 - app.items[7].quality

        val conjuredDegradeRates = LinkedHashMap<String, Int>()
        conjuredDegradeRates["Basic"] = 10 - conjuredApp.items[0].quality
        conjuredDegradeRates["BasicPastSellBy"] = 10 - conjuredApp.items[1].quality
        conjuredDegradeRates["Aged Brie"] = 10 - conjuredApp.items[2].quality //TODO: implement conjured aged brie and backstage passes (int will not work)
        conjuredDegradeRates["Sulfuras"] = 80 - conjuredApp.items[3].quality
        conjuredDegradeRates["Backstage12Days"] = 10 - conjuredApp.items[4].quality
        conjuredDegradeRates["Backstage8Days"] = 10 - conjuredApp.items[5].quality
        conjuredDegradeRates["Backstage2Days"] = 10 - conjuredApp.items[6].quality
        conjuredDegradeRates["BackstageAfterConcert"] = 0 - conjuredApp.items[7].quality

        assertEquals(conjuredDegradeRates["Basic"], 2 * degradeRates["Basic"]!!)
        assertEquals(conjuredDegradeRates["BasicPastSellBy"], 2 * degradeRates["BasicPastSellBy"]!!)
        assertEquals(conjuredDegradeRates["Sulfuras"], 2 * degradeRates["Sulfuras"]!!)
        //assertEquals(conjuredDegradeRates["Backstage12Days"], 2 * degradeRates["Backstage12Days"]!!)
        //assertEquals(conjuredDegradeRates["Backstage8Days"], 2 * degradeRates["Backstage8Days"]!!)
        //assertEquals(conjuredDegradeRates["Backstage2Days"], 2 * degradeRates["Backstage2Days"]!!)
        assertEquals(conjuredDegradeRates["BackstageAfterConcert"], 2 * degradeRates["BackstageAfterConcert"]!!)
        //assertEquals(conjuredDegradeRates["Aged Brie"], 2 * degradeRates["Aged Brie"]!!)

        // get list of items and find their degrade rate and then check that it doubles when conjured
    }

}


