package com.gildedrose

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class GildedRoseTest {

    val repeatNumber = 100

    @Test
    fun checkTimePasses() {
        val items = listOf(Item("BasicItem", 5, 25)) // name, sellIn, quality
        val app = GildedRose(items)

        app.updateQuality()

        assertEquals(app.items[0].sellIn, 4)
        assert(app.items[0].quality < 25)
    }

    @Test
    fun checkQualityDegradesTwiceAsFastAfterSellByDate() {
        val items = listOf(Item("Item", 3, 20)) // name, sellIn, quality
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
        val items = listOf(Item("new item", 4, 21)) // name, sellIn, quality
        val app = GildedRose(items)

        repeat(repeatNumber) {
            app.updateQuality()
            assert(app.items[0].quality >= 0)
        }
    }

    @Test
    fun checkAgedBrieIncreasesInQuality() {
        val items = listOf(Item("Aged Brie", 6, 22)) // name, sellIn, quality
        val app = GildedRose(items)

        app.updateQuality()

        assert(app.items[0].quality > 22)
    }

    @Test
    fun checkQualityIsUnder50() {
        val items = listOf(Item("newitem", 7, 23)) // name, sellIn, quality
        val app = GildedRose(items)

        repeat(repeatNumber) {
            app.updateQuality()
            assert(app.items[0].quality < 50)
        }
    }

    @Test
    fun checkSulfurasNeverHasToBeSold() {
        val items = listOf(Item("Sulfuras, Hand of Ragnaros", 0, 80)) // name, sellIn, quality
        val app = GildedRose(items)

        repeat(repeatNumber) {
            app.updateQuality()
            assertEquals(app.items[0].sellIn, 0)
        }
    }

    @Test
    fun checkSulfurasNeverDecreasesQuality() {
        val items = listOf(Item("Sulfuras, Hand of Ragnaros", 0, 80)) // name, sellIn, quality
        val app = GildedRose(items)

        repeat(repeatNumber) {
            app.updateQuality()
            assertEquals(app.items[0].quality, 80)
        }
    }

    @Test
    fun checkBackstagePassIncreaseInQualityBeforeConcert() {
        val items = listOf(Item("Backstage passes to a TAFKAL80ETC concert", 12, 5)) // name, sellIn, quality
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
        val items = listOf(Item("Backstage passes to a TAFKAL80ETC concert", 12, 5)) // name, sellIn, quality
        val app = GildedRose(items)

        var currentQuality = app.items[0].quality

        repeat(repeatNumber) {
            app.updateQuality()
            if (app.items[0].sellIn < 11 && app.items[0].sellIn > 5){
                assertEquals(app.items[0].quality, currentQuality + 2)
                currentQuality = app.items[0].quality
            }
        }
    }

    @Test
    fun checkBackstagePassQualityIncreasesBy3With5DaysToGo() {
        val items = listOf(Item("Backstage passes to a TAFKAL80ETC concert", 12, 5)) // name, sellIn, quality
        val app = GildedRose(items)

        repeat(repeatNumber) {
            val currentQuality = app.items[0].quality
            app.updateQuality()
            if (app.items[0].sellIn < 5 && app.items[0].sellIn > 0){ // if sellIn < 5, then was < 6 before the update
                assertEquals(app.items[0].quality, currentQuality + 3)
            }

        }
    }

    @Test
    fun checkBackStagePassQualityDropsTo0AfterConcert(){
        val items = listOf(Item("Backstage passes to a TAFKAL80ETC concert", 12, 5)) // name, sellIn, quality
        val app = GildedRose(items)

        repeat(repeatNumber) {
            app.updateQuality()
            if (app.items[0].sellIn < 0){ // sellIn = 0 is day of the concert so can still sell
                assertEquals(app.items[0].quality, 0)
            }
        }
    }



}


