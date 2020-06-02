package product

import org.junit.Test
import product.ProductNames.*
import kotlin.test.assertEquals

class ProductNamesTest {
    @Test
    fun `test all - If this fails I want to see how`() {
        assertEquals("Ziemlich weiß", Milch.description)
        assertEquals("Nur manchmal streichbar", Butter.description)
        assertEquals("Auch nutzbar als Gravitometer", Apfel.description)
        assertEquals("In allen ist was drin", Birne.description)
        assertEquals("Nicht gesund für Papageien", Keks.description)
        assertEquals("Keine Verbindung mit Space Balls©℗®™", Saft.description)
        assertEquals("Ein nützliches Messinstrument", Banane.description)
        assertEquals("Bernd wollte diesen Eintrag entfernen", Brot.description)
        assertEquals("Ungelogen", Kuchen.description)
        assertEquals("Noch sinnfreier als die goldene Variante", Ananas.description)
        assertEquals("AUSSCHLIEẞLICH ZUM VERZEHR BEABSICHTIGT", Gurke.description)
        assertEquals("Auch vefügbar als Ninja-Variante", Zwiebeln.description)
        assertEquals("Frisch aus League of Legends", Salz.description)
    }
}