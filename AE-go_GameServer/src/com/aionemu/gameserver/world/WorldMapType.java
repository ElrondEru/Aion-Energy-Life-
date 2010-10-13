/*
 * This file is part of aion-gates <aion-gates.fr>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.world;

public enum WorldMapType
{
        // Asmodea
        PANDAEMONIUM(120010000),
        MARCHUTAN(120020000),
        ISHALGEN(220010000),
        MORHEIM(220020000),
        ALTGARD(220030000),
        BELUSLAN(220040000),
        BRUSTHONIN(220050000),
        
        // Elysia
        SANCTUM(110010000),
        KAISINEL(110020000),
        POETA(210010000),
        ELTNEN(210020000),
        VERTERON(210030000),
        HEIRON(210040000),
        THEOMOBOS(210060000),
        
        // Balaurea
        INGGISON(210050000),
        GELKMAROS(220070000),   
        SILENTERA_CANYON(600010000),

        // Prison
        PRISON(510010000),
        DF_PRISON(520010000),
        
        RESHANTA(400010000),

        //Instances
		NO_ZONE_NAME(300010000),
		ID_TEST_DUNGEON(300020000),
        NOCHSANA_TRAINING_CAMP(300030000),
        DARK_POETA(300040000),
        ASTERIA_CHAMBER(300050000),
        SULFUR_TREE_NEST(300060000),
        CHAMBER_OF_ROAH(300070000),
        LEFT_WING_CHAMBER(300080000),
        RIGHT_WING_CHAMBER(300090000),
        STEEL_RAKE(300100000),
        DREDGION(300110000),
        KYSIS_CHAMBER(300120000),
        MIREN_CHAMBER(300130000),
        KROTAN_CHAMBER(300140000),
        UDAS_TEMPLE(300150000),
        UDAS_TEMPLE_LOWER(300160000),
        BESHMUNDIR_TEMPLE(300170000),
        TALOCS_HOLLOW(300190000),
		HARAMEL(300200000),
        DREDGION_OF_CHANTRA(300210000),
        CORE(300220000),
        CROMEDE(300230000),
		KARAMATIS(310010000),
		KARAMATIS_B(310020000),
        AERDINA(310030000),
		GERANAIA(310040000),
		AETHEROGENETICS_LAB(310050000),
		FRAGMENT_OF_DARKNESS(310060000),
		IDLF1B_STIGMA(310070000),
		SANCTUM_UNDERGROUND_ARENA(310080000),
        INDRATU_FORTRESS(310090000),
        AZOTURAN_FORTRESS(310100000),
        THEOBOMOS_LAB(310110000),
		IDAB_PRO_L3(310120000),
		ATAXIAR(320010000),
		ATAXIAR_B(320010000),
		BREGIRUN(320030000),
		NIDALBER(320040000),
        ARKANIS_TEMPLE(320050000),
		SPACE_OF_OBLIVION(320060000),
        SPACE_OF_DESTINY(320070000),
		DRAUPNIR_CAVE(320080000),
		TRINIEL_UNDERGROUND_ARENA(320090000),
        FIRE_TEMPLE(320100000),
        ALQUIMIA_RESEARCH_CENTER(320110000),
		SHADOW_COURT_DUNGEON(320120000),
        ADMA_STRONGHOLD(320130000),
		IDAB_PRO_D3(320140000),
        
        //Test Map
        TEST_BASIC(900020000),
        TEST_SERVER(900030000),
        TEST_GIANTMONSTER(900100000),
        HOUSING_BARRACK(900110000);

        private final int worldId;

        WorldMapType(int worldId)
        {
                this.worldId = worldId;
        }

        public int getId()
        {
                return worldId;
        }

        /**
         * @param id of world
         * @return WorldMapType
         */
        public static WorldMapType getWorld(int id)
        {
                for (WorldMapType type : WorldMapType.values())
                {
                        if (type.getId() == id)
                        {
                                return type;
                        }
                }
                return null;
        }
}