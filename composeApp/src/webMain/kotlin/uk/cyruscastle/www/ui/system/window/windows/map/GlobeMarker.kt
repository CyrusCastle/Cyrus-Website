package uk.cyruscastle.www.ui.system.window.windows.map

import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import cyruswebsite.composeapp.generated.resources.Res
import cyruswebsite.composeapp.generated.resources.mapFlagBlue
import cyruswebsite.composeapp.generated.resources.mapFlagCymru
import cyruswebsite.composeapp.generated.resources.mapFlagDarkGreen
import cyruswebsite.composeapp.generated.resources.mapFlagGreen
import cyruswebsite.composeapp.generated.resources.mapFlagKarelia
import cyruswebsite.composeapp.generated.resources.mapFlagKosovo
import cyruswebsite.composeapp.generated.resources.mapFlagOrange
import cyruswebsite.composeapp.generated.resources.mapFlagRed
import cyruswebsite.composeapp.generated.resources.mapFlagScotland
import cyruswebsite.composeapp.generated.resources.mapFlagad
import cyruswebsite.composeapp.generated.resources.mapFlagam
import cyruswebsite.composeapp.generated.resources.mapFlagar
import cyruswebsite.composeapp.generated.resources.mapFlagat
import cyruswebsite.composeapp.generated.resources.mapFlagba
import cyruswebsite.composeapp.generated.resources.mapFlagbe
import cyruswebsite.composeapp.generated.resources.mapFlagca
import cyruswebsite.composeapp.generated.resources.mapFlagch
import cyruswebsite.composeapp.generated.resources.mapFlagcn
import cyruswebsite.composeapp.generated.resources.mapFlagcy
import cyruswebsite.composeapp.generated.resources.mapFlagcz
import cyruswebsite.composeapp.generated.resources.mapFlagde
import cyruswebsite.composeapp.generated.resources.mapFlagdk
import cyruswebsite.composeapp.generated.resources.mapFlagee
import cyruswebsite.composeapp.generated.resources.mapFlages
import cyruswebsite.composeapp.generated.resources.mapFlagfi
import cyruswebsite.composeapp.generated.resources.mapFlaggb
import cyruswebsite.composeapp.generated.resources.mapFlagge
import cyruswebsite.composeapp.generated.resources.mapFlaggl
import cyruswebsite.composeapp.generated.resources.mapFlaggr
import cyruswebsite.composeapp.generated.resources.mapFlagie
import cyruswebsite.composeapp.generated.resources.mapFlagiq
import cyruswebsite.composeapp.generated.resources.mapFlagir
import cyruswebsite.composeapp.generated.resources.mapFlagit
import cyruswebsite.composeapp.generated.resources.mapFlaglt
import cyruswebsite.composeapp.generated.resources.mapFlaglu
import cyruswebsite.composeapp.generated.resources.mapFlaglv
import cyruswebsite.composeapp.generated.resources.mapFlagmd
import cyruswebsite.composeapp.generated.resources.mapFlagmk
import cyruswebsite.composeapp.generated.resources.mapFlagnl
import cyruswebsite.composeapp.generated.resources.mapFlagno
import cyruswebsite.composeapp.generated.resources.mapFlagnp
import cyruswebsite.composeapp.generated.resources.mapFlagro
import cyruswebsite.composeapp.generated.resources.mapFlagrs
import cyruswebsite.composeapp.generated.resources.mapFlagrw
import cyruswebsite.composeapp.generated.resources.mapFlagse
import cyruswebsite.composeapp.generated.resources.mapFlagsk
import cyruswebsite.composeapp.generated.resources.mapFlagsy
import cyruswebsite.composeapp.generated.resources.mapFlagus
import cyruswebsite.composeapp.generated.resources.mapFlagza
import cyruswebsite.composeapp.generated.resources.mapFlagzw
import org.jetbrains.compose.resources.DrawableResource

enum class GlobeMarkerType(
    val flag: DrawableResource,
    val description: String
) {
    LIVED_AT(Res.drawable.mapFlagBlue, "I've lived here..."),
    FREQUENTED(Res.drawable.mapFlagDarkGreen, "I've spent a good amount of time here..."),
    BEEN_TO(Res.drawable.mapFlagGreen, "I've been here..."),
    BEEN_THROUGH(Res.drawable.mapFlagOrange, "I've been here but don't have much to say..."),
    WANT_TO_GO(Res.drawable.mapFlagRed, "I'd like to go here some day...");
}

data class GlobeMarker(
    val name: String,
    val description: String,
    val location: DpOffset,
    val type: GlobeMarkerType,
    val country: DrawableResource? = null,
    val pictures: List<GlobeMarkerPicture> = listOf()
){
    companion object {
        fun getImageDirectory() = "files/map/pictures/"
    }
}

data class GlobeMarkerPicture(
    val fileName: String,
    val contentDescription: String
)

fun getMarkers() = listOf(
    ///////////
    // LIVED //
    ///////////

    // CYMRU
    GlobeMarker(
        name = "Llanfyllin",
        description = "I grew up in Llanfyllin, a rural Welsh town sat on the Powys-Shropshire border. Despite Welsh being prevalent, I unfortunately never felt the need as a child to learn it.",
        location = DpOffset(5226.6665.dp, 2313.3333.dp),
        type = GlobeMarkerType.LIVED_AT,
        country = Res.drawable.mapFlagCymru,
        pictures = listOf(
            GlobeMarkerPicture(
                "meifod church.jpg",
                "A picture I took of the stained glass in Meifod church. This heraldry belonged to ancestors of mine in the Beadnell family of Meifod (later of Llandinam). If I remember correctly, they played some part in rebuilding the church."
            ),
            GlobeMarkerPicture(
                "cadair idris.jpg",
                "There was very little to do in Llanfyllin as a teenager, so between spending hours on the computer I would often walk miles through the hills to other towns. This is not one of those times, this is from a climb up Cadair Idris sometime after starting my undergraduate."
            ),
            GlobeMarkerPicture(
                "llanfyllin cadets.jpg",
                "I joined the Army Cadet Force during high school and stayed on as an adult instructor when I went to university. I taught as an instructor for just over three years, throughout my whole undergraduate."
            ),
            GlobeMarkerPicture(
                "cawl.jpg",
                "I can cook a cawl but I cannot cook a nice cawl."
            ),
        )
    ),
    GlobeMarker(
        name = "Aberystwyth",
        description = "I moved to Aberystwyth to study Computer Science alongside modules in Education. I worked as a demonstrator in the department, and interned as a research assistant in the School of Education before starting a PhD in Education.",
        location = DpOffset(5176.6665.dp, 2323.3333.dp),
        type = GlobeMarkerType.LIVED_AT,
        country = Res.drawable.mapFlagCymru,
        pictures = listOf(
            GlobeMarkerPicture(
                "aberystwyth graduation.jpg",
                "I did not attend my graduation ceremony, but I was very happy to receive an award for achieving the (joint) highest grade average of my cohort!"
            ),
            GlobeMarkerPicture(
                "aberystwyth office.jpg",
                "A picture of me in my office in the university, not sure what I was working on but I was on StackOverflow instead of working on my thesis..."
            ),
            GlobeMarkerPicture(
                "aberystwyth office computer.jpg",
                "A closer picture of the computer I built for the office, I was surprised how few companies made an 'older-looking' chassis."
            ),
            GlobeMarkerPicture(
                "aberystwythKohr.jpg",
                "During my PhD I lived next-door to the former residence of Leopold Kohr, a very inspired man indeed."
            )
        )
    ),

    GlobeMarker(
        name = "Caerdydd",
        description = "Caerdydd and Abergavenny are the first two places that I rough slept, the latter being much more pleasant. Had I been around when it happened, Caerdydd would never have been selected as the capital.",
        location = DpOffset(5180.0.dp, 2376.6665.dp),
        type = GlobeMarkerType.FREQUENTED,
        country = Res.drawable.mapFlagCymru,
        pictures = listOf(
            GlobeMarkerPicture(
                "caerdydd.jpg",
                "A picture of me somewhere in the town centre."
            )
        )
    ),

    //////////
    // BEEN //
    //////////

    GlobeMarker(
        name = "London",
        description = "I have family in London and have spent quite a lot of time all over the city.",
        location = DpOffset(5316.6665.dp, 2373.3333.dp),
        type = GlobeMarkerType.FREQUENTED,
        country = Res.drawable.mapFlaggb,
        pictures = listOf(
            GlobeMarkerPicture(
                "london houseoflords.jpg",
                "I was quite lucky that my uncle, who was quite senior in the Royal College of Nursing, invited me to a dinner with his friend The Baroness Rafferty in the House of Lords dining room."
            ),
        )
    ),

    GlobeMarker(
        name = "Edinburgh",
        description = "I've been to Edinburgh quite a lot, seeing friends & family, and think it's probably the most beautiful city in the UK.",
        location = DpOffset(5213.333.dp, 2156.6665.dp),
        type = GlobeMarkerType.FREQUENTED,
        country = Res.drawable.mapFlagScotland,
//        pictures = listOf(
//            GlobeMarkerPicture(".jpg", "."),
//        )
    ),

    // BELGIUM
    GlobeMarker(
        name = "Belgium",
        description = "Belgium is the first country I ever travelled to by myself, and I loved it. I've only been to Flanders but I've been back since and intend to go again.",
        location = DpOffset(5493.333.dp, 2440.0.dp),
        type = GlobeMarkerType.FREQUENTED,
        country = Res.drawable.mapFlagbe,
        pictures = listOf(
            GlobeMarkerPicture(
                "ghent discovereu.jpg",
                "My first trip, I joined with a group of other young people in Ghent organised around the Gentse Feesten, a festival in the city, and we also went kayaking among other things."
            ),
            GlobeMarkerPicture(
                "gentse feesten.jpg",
                "15th of July, 2022, 21:15, towards the outskirts of the festival. Surprisingly the only photo I took of the festival the first time around."
            ),
            GlobeMarkerPicture(
                "ghent kayaking 1.jpg",
                "I went kayaking in Ghent both times I've been, this is from the second time."
            ),
            GlobeMarkerPicture(
                "ghent kayaking 2.jpg",
                "Gravensteen, restored back to its medieval style for a World's Fair sometime around WW1."
            ),
            GlobeMarkerPicture(
                "ghent kayaking 3.jpg",
                "Het Rabot, a cool fortified bridge/sluice built to reinforce the city after a failed siege by the Imperator Romanorum during some Flemish revolt or other."
            ),
        )
    ),

    // NETHERLANDS
    GlobeMarker(
        name = "Netherlands",
        description = "I happen to have a wide spread of friends in the Netherlands, so it's only natural I've been here a couple of times and have always enjoyed it.",
        location = DpOffset(5516.6665.dp, 2356.6665.dp),
        type = GlobeMarkerType.FREQUENTED,
        country = Res.drawable.mapFlagnl,
        pictures = listOf(
            GlobeMarkerPicture(
                "maaskantje.jpg",
                "On my way to the capital, I stopped at Den Bosch to check it out, and walked the four or five miles to Maaskantje (where this picture is taken) as it was the setting of a Dutch TV show I had seen. It was a pleasant little village & walk. I then walked back to Den Bosch, but all the trains were cancelled so I ended up being driven to Utrecht by some festival-goers and stayed with them a night."
            ),
            GlobeMarkerPicture(
                "amsterdam chess.jpg",
                "Losing at chess in the streets of Amsterdam."
            ),
            GlobeMarkerPicture(
                "groningen chess.jpg",
                "Losing dreadfully at chess at my friend's flat in Groningen."
            ),
            GlobeMarkerPicture(
                "driving to netherlands.jpg",
                "I've been to the Netherlands a couple of times, this picture is of when my friends and I travelled to Gelderland via the channel tunnel."
            ),
            GlobeMarkerPicture(
                "garderen quiz.jpg",
                "Quite like this picture. Had just helped out hosting a quiz among friends in Gelderland."
            ),
            GlobeMarkerPicture(
                "het kantoor.jpg",
                "Het kantoor / the office. Harderwijk."
            ),
        )
    ),

    // GERMANY
    GlobeMarker(
        name = "Kiel",
        description = "Massive deviation from my route so that I could stop at Kiel/Laboe to see the U995.",
        location = DpOffset(5700.0.dp, 2306.6665.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagde,
        pictures = listOf(
            GlobeMarkerPicture(
                "kiel uboat.jpg",
                "The U995."
            ),
            GlobeMarkerPicture(
                "kiel uboat inside.jpg",
                "The U995."
            ),
        )
    ),

    GlobeMarker(
        name = "Bavaria",
        description = "I spent a week in Bavaria, making the most of a 9€ unlimited public transport ticket that the Germans were doing that summer.",
        location = DpOffset(5680.0.dp, 2540.0.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagde,
        pictures = listOf(
            GlobeMarkerPicture(
                "rothenburg.jpg",
                "Rothenburg ob der Tauber was a beautiful town that I had my eyes on when I was planning the trip. It ended up being the first foreign town that I had to rough-sleep in, but it was nice to catch the views of the outskirts when waking up."
            ),
            GlobeMarkerPicture(
                "fussen.jpg",
                "Fussen, near Neuschwanstein. I had seen a painting of Neuschwanstein in the game Civilisation 5 as a child, and had always wanted to go. I walked up the calvary (Kalvarienberg) south of town, and then went to Neuschwanstein. My phone had died by the time I got there, so I snuck into the nearby forests to sleep and spent the next day in McDonalds charging my phone."
            ),
        )
    ),

    GlobeMarker(
        name = "Dublin",
        description = "I have been to Dublin but haven't had the opportunity to really explore. I'd like to go again.",
        location = DpOffset(5053.333.dp, 2280.0.dp),
        type = GlobeMarkerType.BEEN_THROUGH,
        country = Res.drawable.mapFlagie,
    ),

    // DENMARK
    GlobeMarker(
        name = "Jutland",
        description = "I spent a small time in Aarhus, a bit more time in Aalborg, and then travelled to Frederikshavn to get the ferry to Gothenberg.",
        location = DpOffset(5663.333.dp, 2140.0.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagdk,
        pictures = listOf(
            GlobeMarkerPicture(
                "fredrikshamn.jpg",
                "A picture in Frederikshavn, or, in Fredrikshamn? The latter was the Swedish name for the town that was on my ticket and that I ended up internalising."
            ),
        )
    ),

    // SWEDEN
    GlobeMarker(
        name = "Gothenburg",
        description = "I spent a couple of days in Göteborg, mostly just making the most of the public transport (ferries)!",
        location = DpOffset(5736.6665.dp, 2056.6665.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagse,
        pictures = listOf(
            GlobeMarkerPicture(
                "gustavus adolfus.jpg",
                "A statue of Gustavus Adolphus, an important king of the Stormaktstiden, but also the founder of the city."
            ),
            GlobeMarkerPicture(
                "goteborg submarine.jpg",
                "A museum ship submarine, the Nordkaparen."
            ),
            GlobeMarkerPicture(
                "styrsö.jpg",
                "Towards Styrsö on the ferry."
            ),
            GlobeMarkerPicture(
                "köpstadsö.jpg",
                "Community notice board in Köpstadsö."
            ),
            GlobeMarkerPicture(
                "långholmen.jpg",
                "Sat on Långholmen."
            ),
        )
    ),

    GlobeMarker(
        name = "Stockholm",
        description = "For a time I was learning Swedish, and despite being able to pick up the rules of the language well I always struggled with a shallow vocabulary.",
        location = DpOffset(5890.0.dp, 1996.6666.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagse,
        pictures = listOf(
            GlobeMarkerPicture(
                "stockholm me.jpg",
                "A blurry picture of me somewhere in the city centre of Stockholm."
            ),
            GlobeMarkerPicture(
                "risk of crushing.jpg",
                "Risk of crushing!"
            ),
        )
    ),

    // NORWAY
    GlobeMarker(
        name = "Oslo",
        description = "I spent a weekend in Oslo with a group of friends, the city had a few interesting things but what called to me was outside the city.",
        location = DpOffset(5650.0.dp, 1953.3333.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagno,
        pictures = listOf(
            GlobeMarkerPicture(
                "oslo lake.jpg",
                "Sognsvann, just north of Oslo."
            ),
            GlobeMarkerPicture(
                "north of oslo.jpg",
                "The highlight of my trip was getting the train to Movatn and spending the day walking (wading) in the forest through the deep snow..."
            ),
        )
    ),

    // Switzerland
    GlobeMarker(
        name = "Switzerland",
        description = "I arrived in Switzerland completely penniless, being my last stop before home. I was hosted by the incredibly lovely family of a friend of mine in Divonne-les-Bains.",
        location = DpOffset(5590.0.dp, 2628.6665.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagch,
        pictures = listOf(
            GlobeMarkerPicture(
                "switzerland yvoire.jpg",
                "It is hard to pick a highlight for Switzerland, but Yvoire was beautiful and I still often think of it."
            ),
            GlobeMarkerPicture(
                "switzerland yvoire me.jpg",
                "Lake Geneva from the French perspective, sat outside Yvoire. "
            ),
            GlobeMarkerPicture(
                "ile de la harpe.png",
                "A picture I took of Frédérick-César de la Harpe's island, an artificial island, built by the aforementioned who sought to bring the French revolution to Pays de Vaud. I didn't manage to see his obelisk on the island, but Rolle was a beautiful town."
            ),
            GlobeMarkerPicture(
                "divonne les bains.jpg",
                "The war memorial in Divonne-les-Bains, the French town north of Geneva that I stayed in."
            ),
        )
    ),

    // Andorra
    GlobeMarker(
        name = "Andorra",
        description = "I went to Andorra as part of a ski-trip with my high school, it was a great trip and I'd love to ski again if I had the money (and friends who wanted to ski).",
        location = DpOffset(5360.0.dp, 2800.0.dp),
        type = GlobeMarkerType.BEEN_THROUGH,
        country = Res.drawable.mapFlagad
    ),

    // Spain
    GlobeMarker(
        name = "Mallorca",
        description = "I've been to Mallorca twice, and really enjoyed both times.",
        location = DpOffset(5430.0.dp, 2920.0.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlages,
        pictures = listOf(
            GlobeMarkerPicture(
                "mallorca boat.jpg",
                "I used my very impressive level 2 powerboating license to skipper a boat with some friends."
            ),
            GlobeMarkerPicture(
                "alcúdia.jpg",
                "The walls at Alcúdia. We cycled there from Port de Pollensa making the mistake of not going to Pollensa-proper. I was not very good at cycling."
            ),
            GlobeMarkerPicture(
                "mallorca rooftop.jpg",
                "On the rooftops above Palma."
            ),
        )
    ),

    // Czechia
    GlobeMarker(
        name = "Prague",
        description = "I think if I had to pick one European city to realistically live in, I would choose Prague. Cheap, attractive, and it gave me snow in December.",
        location = DpOffset(5816.6665.dp, 2470.0.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagcz,
        pictures = listOf(
            GlobeMarkerPicture(
                "prague in the evening.jpg",
                "A thoroughly impressive looking Christmas market, made unimpressive by the contents of the Christmas market."
            ),
            GlobeMarkerPicture(
                "me in prague.jpg",
                "Me in the Petřín gardens."
            ),
            GlobeMarkerPicture(
                "me above prague.jpg",
                "Above the city of Prague."
            ),
        )
    ),

    // Slovakia
    GlobeMarker(
        name = "Bratislava",
        description = "I am lucky that a good friend of mine lives in Bratislava, and so I've been shown around a couple times.",
        location = DpOffset(5933.333.dp, 2543.3333.dp),
        type = GlobeMarkerType.FREQUENTED,
        country = Res.drawable.mapFlagsk,
        pictures = listOf(
            GlobeMarkerPicture(
                "slavín.jpg",
                "The Soviet war memorial sat above the city."
            ),
            GlobeMarkerPicture(
                "slovakia pub.jpg",
                "My friend and I at a bar."
            ),
            GlobeMarkerPicture(
                "slovakia pajštún.jpg",
                "The top of a lovely hike to Pajštún castle, which, unfortunately and for seemingly no reason, was slighted by Napoleon's armies."
            ),
            GlobeMarkerPicture(
                "slovakia pajštún 2.jpg",
                "My friend and I at Pajštún."
            ),
            GlobeMarkerPicture(
                "bratislava devín.jpg",
                "My friend and I at Devín castle, which, more justifiably but still unfortunately, was slighted by Napoleon's armies."
            ),
            GlobeMarkerPicture(
                "bratislava devín 2.jpg",
                "At Devín. Interesting to note that the site may have been an old Celtic hill-fort."
            ),
        )
    ),

    GlobeMarker(
        name = "Zvolen",
        description = "My Bratislava friend is originally from the centre of Slovakia, and so I spent some time at his family home (commie block) there. Have to say, the interiors of commie blocks are actually quite pleasant.",
        location = DpOffset(5996.6665.dp, 2526.6665.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagsk,
        pictures = listOf(
            GlobeMarkerPicture(
                "zvolen.jpg",
                "At the #Zvolen sign in town, a very attractive town with some interesting history r.e. its role in the Slovak National Uprising."
            ),
            GlobeMarkerPicture(
                "banska stiavnica kalvaria.jpg",
                "The calvary at Banská Štiavnica (nearby to Zvolen), it was very impressive."
            ),
            GlobeMarkerPicture(
                "banska stiavnica from the kalvary.jpg",
                "Looking down at Banská Štiavnica from the calvary."
            ),
            GlobeMarkerPicture(
                "statue at banska stiavnica.jpg",
                "Picture I took of a memorial to the fallen heroes, on the walk into the centre of Banská Štiavnica."
            ),
        )
    ),

    // Bosnia
    GlobeMarker(
        name = "Sarajevo",
        description = "I decided to take a week or so off to have a small journey through Bosnia and Serbia, this started in Sarajevo. I happened to arrive during some kind of National CPR day so I spent some time chatting to students & Red Cross volunteers",
        location = DpOffset(5943.333.dp, 2723.3333.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagba,
        pictures = listOf(
            GlobeMarkerPicture(
                "sarajevo ruins.jpg",
                "An old ruined house sat on the outskirts of Sarajevo."
            ),
            GlobeMarkerPicture(
                "above sarajevo.jpg",
                "Looking down on Sarajevo."
            ),
        )
    ),

    GlobeMarker(
        name = "Zvornik",
        description = "Travelling through Bosnia I had a big stop in a town called Zvornik that I marked down as where I wanted to cross into Serbia.",
        location = DpOffset(5986.6665.dp, 2720.0.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagba,
        pictures = listOf(
            GlobeMarkerPicture(
                "above mali zvornik.jpg",
                "In this picture I am in the fortress above Zvornik, but we are looking across the river (and across the border from the Republika Srpska into Serbia proper) into Mali Zvornik."
            ),
            GlobeMarkerPicture(
                "zvornik walk.jpg",
                "There was certainly a tone shift in how people reacted if you approached them when you crossed into Republika Srpska, not entirely unsurprising, especially considering I am British. Either way, people were not *un*friendly, and the hikes were much more attractive."
            ),
            GlobeMarkerPicture(
                "zvornik fort.jpg",
                "The lovely fortress above Zvornik. Slighted by the Astro-Hungarians, pried from the hands of the Ustascha by Partizanski forces, held by the Bosniaks in the last civil war until eventually being overrun."
            ),
            GlobeMarkerPicture(
                "zvornik fort 2.jpg",
                "Zvornička tvrđava."
            ),
            GlobeMarkerPicture(
                "serbian side of zvornik.jpg",
                "If the Serbs on the 'Bosnia' side of the river were guarded, then the Serbs on the 'Serbia' side of the river were outright distrustful of me. Crossing the border I had my passport thoroughly inspected multiple times and I was grilled on all-sorts before receiving a hesitant 'O.K.', and just half an hour later being stopped by plain clothes police who searched every part of me and my bags looking for who-knows-what. On the train to Belgrade, I had a laugh with the conductor who asked why I didn't have a Welsh accent and why, out of all places, a Welshman would go to 'fucking Zvornik'."
            ),
            GlobeMarkerPicture(
                "japanese zvornik school.jpg",
                "A sign on the Serbian side of the border."
            ),
        )
    ),

    // Serbia
    GlobeMarker(
        name = "Belgrade",
        description = "My plan to visit Bosnia had me cross the border and fly back from Belgrade so I thought I'd spend a couple of days there.",
        location = DpOffset(6023.333.dp, 2690.0.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagrs,
        pictures = listOf(
            GlobeMarkerPicture(
                "belgrade 1.jpg",
                "I was staying right next to the fortress, which was lovely - and it was warm."
            ),
            GlobeMarkerPicture(
                "belgrade 2.jpg",
                "I pretty much worked on my thesis each morning and evening, going out for walks around the fortress in the afternoons."
            ),
            GlobeMarkerPicture(
                "belgrade 3.jpg",
                "I didn't see much else of the city as I was happy enough staying in the fortress each day."
            ),
        )
    ),

    // Romania
    GlobeMarker(
        name = "Bucharest",
        description = "I went to Bucharest for just less than a week and really enjoyed it, but I'd like to leave the city next time and see more of Romania.",
        location = DpOffset(6213.333.dp, 2693.3333.dp),
        type = GlobeMarkerType.BEEN_THROUGH,
        country = Res.drawable.mapFlagro,
        pictures = listOf(
//            GlobeMarkerPicture(".jpg", "."),
        )
    ),

    // Italy
    GlobeMarker(
        name = "Rome",
        description = "I went to Rome when I was relatively young and so I would like to go again to get to know it more intimately.",
        location = DpOffset(5770.0.dp, 2810.0.dp),
        type = GlobeMarkerType.BEEN_THROUGH,
        country = Res.drawable.mapFlagit,
        pictures = listOf(
//            GlobeMarkerPicture(".jpg", "."),
        )
    ),

    // Greece
    GlobeMarker(
        name = "Zakynthos",
        description = "I went to Zakynthos on a family holiday and found it delightful. I think I'd like to, if it's possible, rent a boat for a while and travel between the islands.",
        location = DpOffset(6213.333.dp, 2973.3333.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlaggr,
        pictures = listOf(
//            GlobeMarkerPicture(".jpg", "."),
        )
    ),

    // Cyprus
    GlobeMarker(
        name = "Cyprus",
        description = "There was something in the air of Cyprus that was far too hot for me to cope with. I could only come out in the evening.",
        location = DpOffset(6503.333.dp, 3089.9998.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagcy,
        pictures = listOf(
//            GlobeMarkerPicture(".jpg", "."),
        )
    ),

    // Austria
    GlobeMarker(
        name = "Vienna",
        description = "When I arrived in Vienna, accounting for any costs to get back home, I could only afford a bag of apples, the cheapest pack of sliced meats, and two nights of accommodation - to last me for about 7 days.",
        location = DpOffset(5893.333.dp, 2553.3333.dp),
        type = GlobeMarkerType.BEEN_TO,
        country = Res.drawable.mapFlagat,
        pictures = listOf(
            GlobeMarkerPicture(
                "vienna.jpg",
                "Taken from the window of my accommodation. On day 3, I started to rough sleep in secluded areas of the city and made the absolute most of the free 24/7 transport I had in the city."
            ),
        )
    ),

    //////////
    // WANT //
    //////////

    // AMERICA
    GlobeMarker(
        name = "Greenland",
        description = "It would be interesting to spend some time in the arctic circle, in Greenland, Svalbard, or somewhere else.",
        location = DpOffset(4063.9575.dp, 1171.376.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlaggl
    ),

    GlobeMarker(
        name = "Canada",
        description = "When I was younger I wanted to portage through parts of Canada, although I don't think I'm fit enough for that anymore.",
        location = DpOffset(3193.3333.dp, 1206.6666.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagca
    ),

    GlobeMarker(
        name = "Alaska",
        description = "If I were a rich man I would get a pilot's license and hop between the remote settlements of Alaska from the Alexander Archipelago to the Beaufort sea. Instead, I'll probably take the Alaska Railroad from Anchorage.",
        location = DpOffset(590.0.dp, 1106.6666.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagus
    ),

    GlobeMarker(
        name = "Washington",
        description = "The mountains and meadows of Washington seem unreal.",
        location = DpOffset(1110.0.dp, 2423.3333.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagus
    ),

    GlobeMarker(
        name = "Montana",
        description = "While not quite being Washington, the landscapes around Montana also seem worth a visit.",
        location = DpOffset(1503.3333.dp, 2403.3333.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagus
    ),

    GlobeMarker(
        name = "Patagonia",
        description = "It would be cool to go to Y Wladfa and see the Welsh language in a completely different context - with connotations of prestige and 'Europeanism', compared to the connotations one may find in Wales.",
        location = DpOffset(3149.9998.dp, 5650.0.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagar
    ),

    // Scandinavia
    GlobeMarker(
        name = "Narvik",
        description = "I wanted to take a year abroad during university to study in Tromsø, but never did. It would still be nice to fly to Narvik and travel up the coast to Ráissa and so on.",
        location = DpOffset(5806.6665.dp, 1473.3333.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagno
    ),

    GlobeMarker(
        name = "Finland",
        description = "I wouldn't mind trekking through the fells of Northern Sweden and Finland, but I imagine there's a balance between it being too cold to comfortably wild camp and there being too many mosquitoes in the Finnish erämaa...",
        location = DpOffset(6136.6665.dp, 1559.9999.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagfi
    ),

    GlobeMarker(
        name = "Karelia",
        description = "I find the different heimo (nations) of Finnic people quite interesting, particularly the history of their cultures and how their languages (of which I speak absolutely zero) split-up and developed in parallel.",
        location = DpOffset(6346.6665.dp, 1773.3333.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagKarelia,
        pictures = listOf(
            GlobeMarkerPicture("kalevala.jpg", "I am quite interested in tracing how the interactions between different culture groups influence the later development of those groups. I think no where is this more obvious than when there is a dominant (imperialist) nation and a subjugated nation, which definitely happened here. It's interesting to see what a 'cultural resurgence' looks like, then, in these contexts. I quite like the shift towards Karelianism that seemed to naturally happen when the Finns were in their artistic/cultural renaissance; it feels very real that a people, who are trying to rediscover (or establish) a true national identity, need to explore and borrow from the groups around them to make up an interruption in oral tradition.")
        )
    ),

    // On-Continent
    GlobeMarker(
        name = "Zealand",
        description = "I've been to Denmark, but when I passed through Copenhagen I never left the train station. I ought to rectify that. Also, near Zealand is an island called Ærø which is the setting of one of my favourite books.",
        location = DpOffset(5733.333.dp, 2216.6665.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagdk,
        pictures = listOf(
//            GlobeMarkerPicture("fredrickshamn.jpg", "."),
        )
    ),

    GlobeMarker(
        name = "Luxembourg",
        description = "A cool mix of architecture and geography here, definitely seems worth at least a weekend visit.",
        location = DpOffset(5540.0.dp, 2480.0.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlaglu
    ),

    // Off-Continent
    GlobeMarker(
        name = "Ireland",
        description = "It feels wrong that I've never really experienced Ireland considering I've lived so close for so long.",
        location = DpOffset(4956.6665.dp, 2253.3333.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagie
    ),

    GlobeMarker(
        name = "Highlands",
        description = "I'm going to the highlands for the first time this August.",
        location = DpOffset(5146.6665.dp, 2056.6665.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagScotland
    ),

    // Baltics
    GlobeMarker(
        name = "Lithuania",
        description = "There are some places I've marked out in Lithuania I'd like to see, including Tarakai Island, the Kernave Mounds, ",
        location = DpOffset(6130.0.dp, 2213.3333.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlaglt
    ),

    GlobeMarker(
        name = "Latvia",
        description = "I'm not sure that I'm interested in Riga, but there are a couple of castles I'd like to see: Krimuldas, Sigulda, Cēsis, Turaida, among others.",
        location = DpOffset(6173.333.dp, 2136.6665.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlaglv
    ),

    GlobeMarker(
        name = "Estonia",
        description = "Of all the Baltics, Estonia (particularly Tallinn) interests me the most. It seems wonderful.",
        location = DpOffset(6163.333.dp, 2036.6666.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagee
    ),

    // Balkans etc
    GlobeMarker(
        name = "Pridnestrovie",
        description = "I almost went here instead of Bosnia, I'd like to see the fortresses around Bender and Tiraspol, maybe when I can rent a car down there.",
        location = DpOffset(6313.333.dp, 2570.0.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagmd
    ),

    GlobeMarker(
        name = "Kosovo",
        description = "To the mountains on the western border, the Vragove Vodenice in the south, or perhaps even Mt. Korab in Albania.",
        location = DpOffset(6046.6665.dp, 2786.6665.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagKosovo
    ),

    GlobeMarker(
        name = "North Macedonia",
        description = "I think I'd like to travel through the rural communities south of Skopje, towards Solunska Glava and the forests around Aldinci. Plus the lakes and mountains around Galicica.",
        location = DpOffset(6086.6665.dp, 2823.3333.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagmk
    ),

    // M.E.
    GlobeMarker(
        name = "Georgia",
        description = "All of Georgia, its landscapes, architecture and history, all of it seems incredible. I'd love to spend a couple of months there and travel from east to west.",
        location = DpOffset(6886.6665.dp, 2806.6665.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagge
    ),

    GlobeMarker(
        name = "Armenia",
        description = "I'd like to see some of the architecture like Harichavank and Noravank, but the landscapes around Shahbuz and that southern enclave of Azerbaijan seem incredible as well.",
        location = DpOffset(6893.333.dp, 2860.0.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagam
    ),

    GlobeMarker(
        name = "Rojava",
        description = "I find the political theories of Öcalan particularly fascinating, particularly Apoism and Jineology. I'd like to travel there to see how they manifest in reality rather than just on paper (if things continue with al-Sharaa in power).",
        location = DpOffset(6709.9995.dp, 3050.0.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagsy
    ),

    GlobeMarker(
        name = "Baghdad",
        description = "I'd like to see the landscapes of Iraqi Kurdistan, but also the history of Baghdad as a city of learning is something I'd like to explore.",
        location = DpOffset(6899.9995.dp, 3146.6665.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagiq
    ),

    GlobeMarker(
        name = "Iran",
        description = "As a Cyrus I've always been interested in Iran, I'd like to go to Pasargadae and the tomb of Cyrus the Great, but also I've picked out various spots in the south and along the northern coast of Iran that I'd like to see.",
        location = DpOffset(7156.6665.dp, 3060.0.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagir
    ),

//    GlobeMarker(
//        name = "Afghanistan",
//        description = "...",
//        location = DpOffset(7776.6665.dp, 3076.6665.dp),
//        type = GlobeMarkerType.WANT_TO_GO,
//        country = Res.drawable.mapFlagaf
//    ),

    GlobeMarker(
        name = "Kashmir",
        description = "Kashmir sounds like it's being constantly disputed, I'd like to see what the fuss is all about. More seriously, it looks beautiful from the pictures I've seen.",
        location = DpOffset(8053.333.dp, 3070.0.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        pictures = listOf(
            GlobeMarkerPicture(
                "gold and eagle.jpg",
                "One page of my father's diary when he went to Kashmir, although he didn't have a very good time there."
            ),
            GlobeMarkerPicture(
                "alex and the train.jpg",
                "Somewhere near Shimla in the northern Indian states, my father fell off of a train in a tunnel, hitting his head on the train and having his body scraped between the train and tunnel."
            )
        )
    ),

    GlobeMarker(
        name = "Nepal",
        description = "I don't think I have any urge to climb Everest, but I'd like to try and travel through Nepal.",
        location = DpOffset(8333.333.dp, 3306.6665.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagnp
    ),

    // Africa Proper
//    GlobeMarker(
//        name = "Tunisia",
//        description = "...",
//        location = DpOffset(5673.333.dp, 3056.6665.dp),
//        type = GlobeMarkerType.WANT_TO_GO,
//        country = Res.drawable.mapFlagtn
//    ),
//
//    GlobeMarker(
//        name = "Algeria",
//        description = "...",
//        location = DpOffset(5490.0.dp, 3103.3333.dp),
//        type = GlobeMarkerType.WANT_TO_GO,
//        country = Res.drawable.mapFlagdz
//    ),

    GlobeMarker(
        name = "Rwanda",
        description = "My granny was a missionary in Rwanda during the Hutu revolution and has lots of stories of Rwanda. I'd like to see it.",
        location = DpOffset(6436.6665.dp, 4413.333.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagrw
    ),

    GlobeMarker(
        name = "Zimbabwe",
        description = "My grandad grew up in what was Rhodesia, it would be nice to see where his family was from.",
        location = DpOffset(6443.333.dp, 4960.0.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagzw
    ),

    GlobeMarker(
        name = "KwaZulu-Natal",
        description = "A lovely part of Africa geographically, and I'd like to see where Isandlwana & Rorke's Drift happened.",
        location = DpOffset(6413.333.dp, 5323.333.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagza
    ),

    // Asia Proper
    GlobeMarker(
        name = "Qinghai",
        description = "I watched the Japanese TV show Monkey growing up, which was based on the Journey to the West. It would be cool to follow an approximate of the route, maybe from Xi'an to Qinghai, Dunhuang, over to Kashgar.",
        location = DpOffset(8853.333.dp, 2913.3333.dp),
        type = GlobeMarkerType.WANT_TO_GO,
        country = Res.drawable.mapFlagcn
    ),

//    GlobeMarker(
//        name = "Mongolia",
//        description = "",
//        location = DpOffset(8830.0.dp, 2440.0.dp),
//        type = GlobeMarkerType.WANT_TO_GO,
//        country = Res.drawable.mapFlagmn
//    ),

    // Antarctic
    GlobeMarker(
        name = "Antarctica",
        description = "I wouldn't mind spending a year working as a radio op for the British Antarctic Survey...",
        location = DpOffset(6180.0.dp, 6570.0.dp),
        type = GlobeMarkerType.WANT_TO_GO,
    ),
)