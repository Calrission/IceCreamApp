package com.ege.icecreamapp

class StaticData {
    companion object {

        val groups = arrayListOf(
            GroupSetting("Type of milk", arrayListOf(
                Setting("Whole milk", true),
                Setting("Banana milk", false),
                Setting("Coconut milk", false),
                Setting("Soy milk", false),
                Setting("Almond milk", false),
                Setting("Oat milk", false),
            ), false),
            GroupSetting("Extras", arrayListOf(
                Setting("Whipped Cream +0,5 €", false, 0.5f),
                Setting("Marshmallow +0,75 €", false, 0.75f),
                Setting("Kitkat sprinkling +0,75 €", false, 0.75f),
                Setting("Oreo cookies +0,75 €", false, 0.75f),
            ), true),
        )

        val ice_creams = arrayListOf(
            ModelIceCream(
                "Banoffee",
                "A refreshing combination of our creamy vanilla shake base blended with mango pieces, mango juice and swirled with mango coulis.",
                "Basic", 4.5f, R.drawable.image_1
            ),
            ModelIceCream(
                "Strawberry",
                "A refreshing combination of our creamy vanilla shake base blended with mango pieces, mango juice and swirled with mango coulis.A refreshing combination of our creamy vanilla shake base blended with mango pieces, mango juice and swirled with mango coulis.A refreshing combination of our creamy vanilla shake base blended with mango pieces, mango juice and swirled with mango coulis.A refreshing combination of our creamy vanilla shake base blended with mango pieces, mango juice and swirled with mango coulis.A refreshing combination of our creamy vanilla shake base blended with mango pieces, mango juice and swirled with mango coulis.A refreshing combination of our creamy vanilla shake base blended with mango pieces, mango juice and swirled with mango coulis.",
                "Basic", 5f, R.drawable.image_2
            ),
            ModelIceCream(
                "Oreo",
                "A refreshing combination of our creamy vanilla shake base blended with mango pieces, mango juice and swirled with mango coulis.",
                "Basic", 7.5f, R.drawable.image_6
            ),
            ModelIceCream(
                "Salted Caramel Popcorn ",
                "A refreshing combination of our creamy vanilla shake base blended with mango pieces, mango juice and swirled with mango coulis.",
                "Cinema", 6f, R.drawable.image_5
            ),
            ModelIceCream(
                "Black Forest Shake",
                "We have paired our creamy chocolate shake with chocolate biscuits, cherry and black forest syrups to create the perfect match in this truly decadent shake. Add whipped cream drizzled with both chocolate sauce and our house-made cherry coulis, topped with a cherry for the ultimate treat!",
                "Special", 10f, R.drawable.image_4
            ),
            ModelIceCream(
                "Biscoff",
                "A refreshing combination of our creamy vanilla shake base blended with mango pieces, mango juice and swirled with mango coulis.",
                "Basic", 5.5f, R.drawable.image_3
            ),
        )

        val groupsSettings = mapOf(
            "Type of milk" to arrayListOf("Whole milk", "Banana milk", "Coconut milk", "Soy milk", "Almond milk", "Oat milk"),
            "Extras" to arrayListOf("Whipped Cream +0,5 €", "Marshmallow +0,75 €", "Kitkat sprinkling +0,75 €", "Oreo cookies +0,75 €"),
        )
    }
}