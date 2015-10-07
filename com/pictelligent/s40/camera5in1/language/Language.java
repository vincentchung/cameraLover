
package com.pictelligent.s40.camera5in1.language;

import com.pictelligent.s40.camera5in1.DebugUtil;
import com.pictelligent.s40.camera5in1.Camera5in1;

/**
 * Language resources manager. We can manage all UI resources here.
 */
public class Language {
    // Target: EFIGS, Portuguese, Arabic, Traditional Chinese, Simplified
    // Chinese, Polish, Russian, LTA Spanish and Brazilian Portuguese
    public static final String LANGUAGUE_ENGLISH = "en";
    public static final String LANGUAGUE_FRENCH = "fr";
    public static final String LANGUAGUE_ITALIAN = "it";
    public static final String LANGUAGUE_GERMAN = "de";
    public static final String LANGUAGUE_SPANISH = "es-ES";
    public static final String LANGUAGUE_PORTUGESE = "pt-PT";
    public static final String LANGUAGUE_ARABIC = "ar";
    public static final String LANGUAGUE_ZHTW = "zh-TW";
    public static final String LANGUAGUE_ZHHK = "zh-HK";
    public static final String LANGUAGUE_ZHCN = "zh-CN";
    public static final String LANGUAGUE_POLISH = "pl-PL";
    public static final String LANGUAGUE_RUSSIAN = "ru-RU";
    public static final String LANGUAGUE_LTA_SPANISH = "es-US";
    public static final String LANGUAGUE_BRA_PORTUGESE = "pt-BR";
    public static final String LANGUAGUE_TURKISH = "tr-TR";
    
    public static final int TEXT_RETRO = 0;
    public static final int TEXT_FUSION = 1;
    public static final int TEXT_COLLAGE = 2;
    public static final int TEXT_TWIST = 3;
    public static final int TEXT_FUTURO = 4;
    public static final int TEXT_COLLAGE_MODES = 5;
    public static final int TEXT_COLLAGE_MODES_1 = TEXT_COLLAGE_MODES + 1;
    public static final int TEXT_COLLAGE_MODES_2 = TEXT_COLLAGE_MODES_1 + 1;
    public static final int TEXT_COLLAGE_MODES_3 = TEXT_COLLAGE_MODES_2 + 1;
    public static final int TEXT_COLLAGE_MODES_4 = TEXT_COLLAGE_MODES_3 + 1;
    public static final int TEXT_COLLAGE_MODES_5 = TEXT_COLLAGE_MODES_4 + 1;
    public static final int TEXT_COLLAGE_MODES_6 = TEXT_COLLAGE_MODES_5 + 1;
    public static final int TEXT_FUSION_MODES = TEXT_COLLAGE_MODES_6 + 1;
    public static final int TEXT_FUSION_MODES_1 = TEXT_FUSION_MODES + 1;
    public static final int TEXT_FUSION_MODES_2 = TEXT_FUSION_MODES_1 + 1;
    public static final int TEXT_FUSION_MODES_3 = TEXT_FUSION_MODES_2 + 1;
    public static final int TEXT_FUSION_MODES_4 = TEXT_FUSION_MODES_3 + 1;
    public static final int TEXT_FUSION_MODES_5 = TEXT_FUSION_MODES_4 + 1;
    public static final int TEXT_FUSION_MODES_6 = TEXT_FUSION_MODES_5 + 1;
    public static final int TEXT_FUSION_HELP = TEXT_FUSION_MODES_6 + 1;
    public static final int TEXT_EFFECT_NONE = TEXT_FUSION_HELP + 1;
    public static final int TEXT_EFFECT_COLD = TEXT_EFFECT_NONE + 1;
    public static final int TEXT_EFFECT_WARM = TEXT_EFFECT_COLD + 1;
    public static final int TEXT_EFFECT_LOMO = TEXT_EFFECT_WARM + 1;
    public static final int TEXT_EFFECT_OLD_FILM = TEXT_EFFECT_LOMO + 1;
    public static final int TEXT_EFFECT_BLEACH = TEXT_EFFECT_OLD_FILM + 1;
    public static final int TEXT_EFFECT_VIGNETTE = TEXT_EFFECT_BLEACH + 1;
    public static final int TEXT_EFFECT_ANTIQUE = TEXT_EFFECT_VIGNETTE + 1;

    public static final int TEXT_TWIST_WATERDROP = TEXT_EFFECT_ANTIQUE + 1;
    public static final int TEXT_TWIST_WAVE = TEXT_TWIST_WATERDROP + 1;
    public static final int TEXT_TWIST_TWIRL_LEFT = TEXT_TWIST_WAVE + 1;
    public static final int TEXT_TWIST_TWIRL_RIGHT = TEXT_TWIST_TWIRL_LEFT + 1;
    public static final int TEXT_TWIST_SQUEEZE = TEXT_TWIST_TWIRL_RIGHT + 1;
    public static final int TEXT_TWIST_BUBBLE = TEXT_TWIST_SQUEEZE + 1;

    public static final int TEXT_FUTURO_EFFECT1 = TEXT_TWIST_BUBBLE + 1;
    public static final int TEXT_FUTURO_EFFECT2 = TEXT_FUTURO_EFFECT1 + 1;
    public static final int TEXT_FUTURO_EFFECT3 = TEXT_FUTURO_EFFECT2 + 1;
    public static final int TEXT_FUTURO_EFFECT4 = TEXT_FUTURO_EFFECT3 + 1;
    public static final int TEXT_FUTURO_EFFECT5 = TEXT_FUTURO_EFFECT4 + 1;
    public static final int TEXT_FUTURO_EFFECT6 = TEXT_FUTURO_EFFECT5 + 1;

    public static final int TEXT_ABOUT = TEXT_FUTURO_EFFECT6 + 1;
    public static final int TEXT_ABOUT2 = TEXT_ABOUT + 1;

    public static final int TEXT_MENU_ABOUT = TEXT_ABOUT2 + 1;
    public static final int TEXT_MENU_NOKIASTORE = TEXT_MENU_ABOUT + 1;
    public static final int TEXT_MENU_LEGAL = TEXT_MENU_NOKIASTORE + 1;
    public static final int TEXT_MENU_BACK = TEXT_MENU_LEGAL + 1;
    public static final int TEXT_MENU_HELP = TEXT_MENU_BACK + 1;
    public static final int TEXT_MAIN_HELP = TEXT_MENU_HELP + 1;

    public static final int TEXT_LEGAL_INFO = TEXT_MAIN_HELP + 1;
    public static final int TEXT_SAVING = TEXT_LEGAL_INFO + 1;
    public static final int TEXT_APPNAME = TEXT_SAVING + 1;
    public static final int TEXT_ALLRIGHTSRESERVED = TEXT_APPNAME + 1;
    public static final int TEXT_CAMERALOVER = TEXT_ALLRIGHTSRESERVED + 1;
    public static final int TEXT_JPEGGROUP = TEXT_CAMERALOVER + 1;
    public static final int TEXT_FUSION_POPUP = TEXT_JPEGGROUP + 1;
    public static final int TEXT_MEMORYFULL = TEXT_FUSION_POPUP + 1;
    public static final int TEXT_MENU_EULA = TEXT_MEMORYFULL + 1;

    public static final int TEXT_MENU_SELECT = TEXT_MENU_EULA + 1;
    
    public static final int TEXT_NUMBER = TEXT_MENU_SELECT + 1;
    public static String lang="";

    
    private static String[] texts = new String[TEXT_NUMBER];
    
    public static String getLanguage()
    {
    	return lang;
    }

    /**
     * Loading language. Default is English.
     */
    public static void loadLanguage(String language) {
        if (Camera5in1.mDebug)
            DebugUtil.Log("Loading language set " + language);
        
        lang=language;

        if (LANGUAGUE_ENGLISH.equalsIgnoreCase(language)) {
            loadENUS();
        } else if (LANGUAGUE_FRENCH.equalsIgnoreCase(language)) {
            loadFR();
        } else if (LANGUAGUE_ITALIAN.equalsIgnoreCase(language)) {
            loadIT();
        } else if (LANGUAGUE_GERMAN.equalsIgnoreCase(language)) {
            loadDE();
        } else if (LANGUAGUE_SPANISH.equalsIgnoreCase(language)) {
            loadES();
        } else if (LANGUAGUE_LTA_SPANISH.equalsIgnoreCase(language)) { // TODO: this is not correct
            loadES();	
        } else if (LANGUAGUE_PORTUGESE.equalsIgnoreCase(language)) {
            loadPTPT();
        } else if (LANGUAGUE_BRA_PORTUGESE.equalsIgnoreCase(language)) { // TODO: this is not correct
            loadPTBR();
        } else if (LANGUAGUE_ARABIC.equalsIgnoreCase(language)) {
            loadAR();
        } else if (LANGUAGUE_ZHTW.equalsIgnoreCase(language)) {
            loadZHTW();
        }else if (LANGUAGUE_ZHHK.equalsIgnoreCase(language)) {
            loadZHTW();
        } else if (LANGUAGUE_ZHCN.equalsIgnoreCase(language)) {
            loadZHCN();
        } else if (LANGUAGUE_POLISH.equalsIgnoreCase(language)) {
            loadPLPL();
        } else if (LANGUAGUE_RUSSIAN.equalsIgnoreCase(language)) {
            loadRU();
        } else if (LANGUAGUE_RUSSIAN.equalsIgnoreCase(language)) {
            loadRU();
        } else if (LANGUAGUE_TURKISH.equalsIgnoreCase(language)) {
            loadTRTR();
        } 
        else {
            loadENUS();
        }
    }

    /**
     * Load language for English.
     */
    private static void loadENUS() {
        texts[TEXT_RETRO] = "Retro";
        texts[TEXT_FUSION] = "Fusion";
        texts[TEXT_COLLAGE] = "Collage";
        texts[TEXT_TWIST] = "Twist";
        texts[TEXT_FUTURO] = "Futuro";
        texts[TEXT_COLLAGE_MODES] = "Collage Layouts";
        texts[TEXT_COLLAGE_MODES_1] = "Layout 1";
        texts[TEXT_COLLAGE_MODES_2] = "Layout 2";
        texts[TEXT_COLLAGE_MODES_3] = "Layout 3";
        texts[TEXT_COLLAGE_MODES_4] = "Layout 4";
        texts[TEXT_COLLAGE_MODES_5] = "Layout 5";
        texts[TEXT_COLLAGE_MODES_6] = "Layout 6";
        texts[TEXT_FUSION_MODES] = "Fusion Layouts";
        texts[TEXT_FUSION_MODES_1] = "Layout 1";
        texts[TEXT_FUSION_MODES_2] = "Layout 2";
        texts[TEXT_FUSION_MODES_3] = "Layout 3";
        texts[TEXT_FUSION_MODES_4] = "Layout 4";
        texts[TEXT_FUSION_MODES_5] = "Layout 5";
        texts[TEXT_FUSION_MODES_6] = "Layout 6";
        texts[TEXT_FUSION_HELP] = "This is the help document of fusion";

        texts[TEXT_EFFECT_NONE] = "No effect";
        texts[TEXT_EFFECT_COLD] = "Cold";
        texts[TEXT_EFFECT_WARM] = "Warm";
        texts[TEXT_EFFECT_LOMO] = "Lomo";
        texts[TEXT_EFFECT_OLD_FILM] = "Old film";
        texts[TEXT_EFFECT_BLEACH] = "Bleach";
        texts[TEXT_EFFECT_VIGNETTE] = "Vignette";
        texts[TEXT_EFFECT_ANTIQUE] = "Antique";

        texts[TEXT_TWIST_WAVE] = "Wave";
        texts[TEXT_TWIST_TWIRL_LEFT] = "Twirl left";
        texts[TEXT_TWIST_TWIRL_RIGHT] = "Twirl right";
        texts[TEXT_TWIST_WATERDROP] = "Water drop";
        texts[TEXT_TWIST_SQUEEZE] = "Squeeze";
        texts[TEXT_TWIST_BUBBLE] = "Bubble";

        texts[TEXT_FUTURO_EFFECT1] = "X-ray";
        texts[TEXT_FUTURO_EFFECT2] = "Squares";
        texts[TEXT_FUTURO_EFFECT3] = "Blue";
        texts[TEXT_FUTURO_EFFECT4] = "Space";
        texts[TEXT_FUTURO_EFFECT5] = "Virtual";
        texts[TEXT_FUTURO_EFFECT6] = "Neon";

        texts[TEXT_MENU_ABOUT] = "About Camera 5-in-1";
        texts[TEXT_MENU_NOKIASTORE] = "More games";
        texts[TEXT_MENU_LEGAL] = "Legal info";
        texts[TEXT_MENU_BACK] = "Back";
        texts[TEXT_MENU_HELP] = "Help";
        texts[TEXT_MAIN_HELP] = "Welcome to Pictelligent Camera 5-in-1!\n\n"
                + "This application has five different modes that let you capture exciting images in different ways.\n\n"
                + "Retro: after capturing, you can apply effects to give your photos an old-fashioned look.\n\n"
                + "Collage: select a layout and capture images to create your favorite photo collage.\n\n"
                + "Twist: capture an image and make it look funny with twist effects.\n\n"
                + "Fusion: select a layout and capture two images; we put them together.\n\n"
                + "Futuro: capture an image and make it futuristic with our digital tech effects.";
        texts[TEXT_SAVING] = "Saving";
        texts[TEXT_APPNAME] = "Camera 5-in-1";
        texts[TEXT_ALLRIGHTSRESERVED] = "All rights reserved";
        texts[TEXT_CAMERALOVER] = "I am a camera lover";
        texts[TEXT_JPEGGROUP] = "This software is based in part on the work of the Independent JPEG Group";
        texts[TEXT_FUSION_POPUP] = "Capture this area";
        
        texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
        texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
        texts[TEXT_MEMORYFULL] = "Memory full";
        texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
 	    texts[TEXT_MENU_EULA] = "License Terms";
        texts[TEXT_MENU_SELECT] = "Select";
    }

    /**
     * Load language for French.
     */
    private static void loadFR() {
texts[TEXT_RETRO] ="Rétro";
texts[TEXT_FUSION] = "Fusion";
texts[TEXT_COLLAGE] ="Collage";
texts[TEXT_TWIST] = "Vrille";
texts[TEXT_FUTURO] = "Futuro";
texts[TEXT_COLLAGE_MODES] = "Modèles de collage";
texts[TEXT_COLLAGE_MODES_1] = "Modèle 1";
texts[TEXT_COLLAGE_MODES_2] = "Modèle 2";
texts[TEXT_COLLAGE_MODES_3] = "Modèle 3";
texts[TEXT_COLLAGE_MODES_4] = "Modèle 4";
texts[TEXT_COLLAGE_MODES_5] = "Modèle 5";
texts[TEXT_COLLAGE_MODES_6] = "Modèle 6";
texts[TEXT_FUSION_MODES] = "Modèles de fusion";
texts[TEXT_FUSION_MODES_1] = "Modèle 1";
texts[TEXT_FUSION_MODES_2] = "Modèle 2";
texts[TEXT_FUSION_MODES_3] = "Modèle 3";
texts[TEXT_FUSION_MODES_4] = "Modèle 4";
texts[TEXT_FUSION_MODES_5] = "Modèle 5";
texts[TEXT_FUSION_MODES_6] = "Modèle 6";
texts[TEXT_EFFECT_NONE] = "Pas d'effet";
texts[TEXT_EFFECT_COLD] = "Froid";
texts[TEXT_EFFECT_WARM] = "Chaud";
texts[TEXT_EFFECT_LOMO] = "Lomo";
texts[TEXT_EFFECT_OLD_FILM] = "Anc.film";
texts[TEXT_EFFECT_BLEACH] = "Blanchiment";
texts[TEXT_EFFECT_VIGNETTE] = "Dégradé";
texts[TEXT_EFFECT_ANTIQUE] = "Antique";
texts[TEXT_TWIST_WAVE] = "Vague";
texts[TEXT_TWIST_TWIRL_LEFT] = "Vrille gauche";
texts[TEXT_TWIST_TWIRL_RIGHT] = "Vrille droite";
texts[TEXT_TWIST_WATERDROP] = "Goutte eau";
texts[TEXT_TWIST_SQUEEZE] = "Compression";
texts[TEXT_TWIST_BUBBLE] = "Bulle";
texts[TEXT_FUTURO_EFFECT1] = "Rayons X";
texts[TEXT_FUTURO_EFFECT2] = "Carrés";
texts[TEXT_FUTURO_EFFECT3] = "Bleu";
texts[TEXT_FUTURO_EFFECT4] = "Espace";
texts[TEXT_FUTURO_EFFECT5] = "Virtuel";
texts[TEXT_FUTURO_EFFECT6] = "Néon";
texts[TEXT_MENU_ABOUT] = "À propos de Caméra 5en1";
texts[TEXT_MENU_NOKIASTORE] = "Plus de jeux";
texts[TEXT_MENU_LEGAL] = "Conditions licence";
texts[TEXT_MENU_BACK] = "Retour";
texts[TEXT_MENU_HELP] = "Aide";
texts[TEXT_MAIN_HELP] = "Bienvenue dans Pictelligent Caméra 5 en 1!\n\n"
+ "Cette application offre cinq modes différents vous permettant de prendre d'extraordinaires photos de différentes manières.\n\n"
+ "Rétro : prenez une photo puis appliquez des effets pour lui donner un air rétro.\n\n"
+ "Collage : sélectionnez un modèle et prenez une photo pour créer votre collage photo préféré.\n\n"
+ "Vrille : prenez une photo et donnez-lui un effet tourbillonnant.\n\n"
+ "Fusion : sélectionnez un modèle, prenez deux images et fusionnez-les.\n\n"
+ "Futuro : prenez une photo et rendez-la futuriste en appliquant nos effets techniques numériques!";
texts[TEXT_SAVING] = "Enreg…";
texts[TEXT_APPNAME] = "Caméra 5 en 1";
texts[TEXT_ALLRIGHTSRESERVED] = "Tous droits réservés";
texts[TEXT_CAMERALOVER] = "J'aime les caméras";
texts[TEXT_JPEGGROUP] = "Ce logiciel est le résultat des travaux du groupe indépendant JPEG Group";
texts[TEXT_FUSION_POPUP] = "NOT USED!";
texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MEMORYFULL] = "Mémoire pleine";
texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MENU_EULA] = "Conditions licence";
texts[TEXT_MENU_SELECT] = "Sélect.";
}


    /**
     * Load language for Italian.
     */
 
private static void loadIT() {
texts[TEXT_RETRO] ="Retrò";
texts[TEXT_FUSION] = "Fusione  ";
texts[TEXT_COLLAGE] ="Collage";
texts[TEXT_TWIST] = "Torsione";
texts[TEXT_FUTURO] = "Futuro";
texts[TEXT_COLLAGE_MODES] = "Layout collage";
texts[TEXT_COLLAGE_MODES_1] = "Layout 1";
texts[TEXT_COLLAGE_MODES_2] = "Layout 2";
texts[TEXT_COLLAGE_MODES_3] = "Layout 3";
texts[TEXT_COLLAGE_MODES_4] = "Layout 4";
texts[TEXT_COLLAGE_MODES_5] = "Layout 5";
texts[TEXT_COLLAGE_MODES_6] = "Layout 6";
texts[TEXT_FUSION_MODES] = "Layout Fusione ";
texts[TEXT_FUSION_MODES_1] = "Layout 1";
texts[TEXT_FUSION_MODES_2] = "Layout 2";
texts[TEXT_FUSION_MODES_3] = "Layout 3";
texts[TEXT_FUSION_MODES_4] = "Layout 4";
texts[TEXT_FUSION_MODES_5] = "Layout 5";
texts[TEXT_FUSION_MODES_6] = "Layout 6";
texts[TEXT_EFFECT_NONE] = "Nessun effetto";
texts[TEXT_EFFECT_COLD] = "Freddo";
texts[TEXT_EFFECT_WARM] = "Caldo";
texts[TEXT_EFFECT_LOMO] = "Lomo";
texts[TEXT_EFFECT_OLD_FILM] = "Vecchio film";
texts[TEXT_EFFECT_BLEACH] = "Bleach";
texts[TEXT_EFFECT_VIGNETTE] = "Vignettatura";
texts[TEXT_EFFECT_ANTIQUE] = "Antico";
texts[TEXT_TWIST_WAVE] = "Onda";
texts[TEXT_TWIST_TWIRL_LEFT] = "Vortice a sinistra";
texts[TEXT_TWIST_TWIRL_RIGHT] = "Vortice a destra";
texts[TEXT_TWIST_WATERDROP] = "Goccia d'acqua";
texts[TEXT_TWIST_SQUEEZE] = "Schiaccia";
texts[TEXT_TWIST_BUBBLE] = "Bolle";
texts[TEXT_FUTURO_EFFECT1] = "Raggi X";
texts[TEXT_FUTURO_EFFECT2] = "Quadri";
texts[TEXT_FUTURO_EFFECT3] = "Blu";
texts[TEXT_FUTURO_EFFECT4] = "Spazio";
texts[TEXT_FUTURO_EFFECT5] = "Virtuale";
texts[TEXT_FUTURO_EFFECT6] = "Neon";
texts[TEXT_MENU_ABOUT] = "Sulla Camera 5-in-1";
texts[TEXT_MENU_NOKIASTORE] = "Altri giochi";
texts[TEXT_MENU_LEGAL] = "Condizioni licenza";
texts[TEXT_MENU_BACK] = "Indietro";
texts[TEXT_MENU_HELP] = "Aiuto";
texts[TEXT_MAIN_HELP] = "Benvenuto in Pictelligent Camera 5-in-1!\n\n"
+"Questa applicazione ha cinque diverse modalità che Le permettono di scattare fantastiche immagini in vari modi.\n\n"
+"Retrò: dopo lo scatto potrà applicare gli effetti per ottenere un look all'antica.\n\n"
+"Collage: selezioni un layout e scatti delle immagini per creare il Suo collage fotografico preferito.\n\n"
+"Torsione: scatti una foto e aggiunga un vortice con effetti torsione.\n\n"
+"Fusione: selezioni un layout e scatti due immagini; noi le uniamo.\n\n"
+"Futuro: selezioni un'immagine e la renda futuristica con i nostri effetti di tecnica digitale.";
texts[TEXT_SAVING] = "Salvataggio in corso…";
texts[TEXT_APPNAME] = "Camera 5-in-1";
texts[TEXT_ALLRIGHTSRESERVED] = "Tutti i diritti riservati";
texts[TEXT_CAMERALOVER] = "Sono appassionato di fotocamere";
texts[TEXT_JPEGGROUP] = "Questo software si basa in parte sul lavoro del Gruppo JPEG indipendente";
texts[TEXT_FUSION_POPUP] = "NOT USED!";
texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MEMORYFULL] = "Memoria piena";
texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MENU_EULA] = "Condizioni licenza";
texts[TEXT_MENU_SELECT] = "Seleziona";
}

   
    /**
     * Load language for German.
     */
 
private static void loadDE() {
texts[TEXT_RETRO] ="Retro";
texts[TEXT_FUSION] = "Fusion";
texts[TEXT_COLLAGE] ="Collage";
texts[TEXT_TWIST] = "Wirbel";
texts[TEXT_FUTURO] = "Futuro";
texts[TEXT_COLLAGE_MODES] = "Collagen-Layouts";
texts[TEXT_COLLAGE_MODES_1] = "Layout 1";
texts[TEXT_COLLAGE_MODES_2] = "Layout 2";
texts[TEXT_COLLAGE_MODES_3] = "Layout 3";
texts[TEXT_COLLAGE_MODES_4] = "Layout 4";
texts[TEXT_COLLAGE_MODES_5] = "Layout 5";
texts[TEXT_COLLAGE_MODES_6] = "Layout 6";
texts[TEXT_FUSION_MODES] = "Fusions-Layouts";
texts[TEXT_FUSION_MODES_1] = "Layout 1";
texts[TEXT_FUSION_MODES_2] = "Layout 2";
texts[TEXT_FUSION_MODES_3] = "Layout 3";
texts[TEXT_FUSION_MODES_4] = "Layout 4";
texts[TEXT_FUSION_MODES_5] = "Layout 5";
texts[TEXT_FUSION_MODES_6] = "Layout 6";
texts[TEXT_EFFECT_NONE] = "Kein Effekt";
texts[TEXT_EFFECT_COLD] = "Kalt";
texts[TEXT_EFFECT_WARM] = "Warm";
texts[TEXT_EFFECT_LOMO] = "Lomo";
texts[TEXT_EFFECT_OLD_FILM] = "Alter Film";
texts[TEXT_EFFECT_BLEACH] = "Bleichen";
texts[TEXT_EFFECT_VIGNETTE] = "Vignette";
texts[TEXT_EFFECT_ANTIQUE] = "Antik";
texts[TEXT_TWIST_WAVE] = "Welle";
texts[TEXT_TWIST_TWIRL_LEFT] = "Linksdreh";
texts[TEXT_TWIST_TWIRL_RIGHT] = "Rechtsdreh";
texts[TEXT_TWIST_WATERDROP] = "Wasserperle";
texts[TEXT_TWIST_SQUEEZE] = "Quetschen";
texts[TEXT_TWIST_BUBBLE] = "Blase";
texts[TEXT_FUTURO_EFFECT1] = "Röntgen";
texts[TEXT_FUTURO_EFFECT2] = "Quadrate";
texts[TEXT_FUTURO_EFFECT3] = "Blau";
texts[TEXT_FUTURO_EFFECT4] = "Raum";
texts[TEXT_FUTURO_EFFECT5] = "Virtuell";
texts[TEXT_FUTURO_EFFECT6] = "Neon";
texts[TEXT_MENU_ABOUT] = "Über die 5-in-1-Kamera";
texts[TEXT_MENU_NOKIASTORE] = "Mehr Spiele";
texts[TEXT_MENU_LEGAL] = "Lizenzbedingungen";
texts[TEXT_MENU_BACK] = "Zurück";
texts[TEXT_MENU_HELP] = "Hilfe";
texts[TEXT_MAIN_HELP] = "Willkommen zur Pictelligent 5-in-1-Kamera!\n\n"
+"Diese Applikation hat fünf Modi, um Fotos auf verschiedene Weisen zu schießen.\n\n"
+"Retro: Nach der Aufnahme, Effekte anwenden, um den Fotos einen altmodischen Touch zu geben.\n\n"
+"Collage: Layout wählen und Fotos schießen, um eigene Collage zu erstellen.\n\n"
+"Wirbel: Foto schießen und diesem Drall und Wirbeleffekte geben.\n\n"
+"Fusion: Layout wählen und zwei Fotos schießen. Wir setzen sie zusammen.\n\n"
+"Futuro: Foto schießen und mit unseren digitalen Effekten futuristisch bearbeiten.";
texts[TEXT_SAVING] = "Speichern…";
texts[TEXT_APPNAME] = "5-in-1-Kamera";
texts[TEXT_ALLRIGHTSRESERVED] = "Alle Rechte vorbehalten";
texts[TEXT_CAMERALOVER] = "Ich liebe Kameras";
texts[TEXT_JPEGGROUP] = "Diese Software basiert teilweise auf der Arbeit der unabhängigen JPEG-Gruppe";
texts[TEXT_FUSION_POPUP] = "NOT USED!";
texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MEMORYFULL] = "Speicher voll";
texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MENU_EULA] = "Lizenzbedingungen";
texts[TEXT_MENU_SELECT] = "Auswahl";
}


    /**
     * Load language for Spanish.
     */

private static void loadES() {
texts[TEXT_RETRO] ="Retro";
texts[TEXT_FUSION] = "Fusion";
texts[TEXT_COLLAGE] ="Collage";
texts[TEXT_TWIST] = "Twist";
texts[TEXT_FUTURO] = "Futuro";
texts[TEXT_COLLAGE_MODES] = "Diseños Collage";
texts[TEXT_COLLAGE_MODES_1] = "Diseño 1";
texts[TEXT_COLLAGE_MODES_2] = "Diseño 2";
texts[TEXT_COLLAGE_MODES_3] = "Diseño 3";
texts[TEXT_COLLAGE_MODES_4] = "Diseño 4";
texts[TEXT_COLLAGE_MODES_5] = "Diseño 5";
texts[TEXT_COLLAGE_MODES_6] = "Diseño 6";
texts[TEXT_FUSION_MODES] = "Diseños Fusion";
texts[TEXT_FUSION_MODES_1] = "Diseño 1";
texts[TEXT_FUSION_MODES_2] = "Diseño 2";
texts[TEXT_FUSION_MODES_3] = "Diseño 3";
texts[TEXT_FUSION_MODES_4] = "Diseño 4";
texts[TEXT_FUSION_MODES_5] = "Diseño 5";
texts[TEXT_FUSION_MODES_6] = "Diseño 6";
texts[TEXT_EFFECT_NONE] = "Sin efectos";
texts[TEXT_EFFECT_COLD] = "Frío";
texts[TEXT_EFFECT_WARM] = "Cálido";
texts[TEXT_EFFECT_LOMO] = "Lomo";
texts[TEXT_EFFECT_OLD_FILM] = "Película antigua";
texts[TEXT_EFFECT_BLEACH] = "Blanquear";
texts[TEXT_EFFECT_VIGNETTE] = "Viñeta";
texts[TEXT_EFFECT_ANTIQUE] = "Foto antigua";
texts[TEXT_TWIST_WAVE] = "Onda";
texts[TEXT_TWIST_TWIRL_LEFT] = "Molinete a la izquierda";
texts[TEXT_TWIST_TWIRL_RIGHT] = "Molinete a la derecha";
texts[TEXT_TWIST_WATERDROP] = "Gota de agua";
texts[TEXT_TWIST_SQUEEZE] = "Encoger";
texts[TEXT_TWIST_BUBBLE] = "Burbuja";
texts[TEXT_FUTURO_EFFECT1] = "Rayos X";
texts[TEXT_FUTURO_EFFECT2] = "Cuadrados";
texts[TEXT_FUTURO_EFFECT3] = "Azul";
texts[TEXT_FUTURO_EFFECT4] = "Espacio";
texts[TEXT_FUTURO_EFFECT5] = "Virtual";
texts[TEXT_FUTURO_EFFECT6] = "Neón";
texts[TEXT_MENU_ABOUT] = "Acerca de Cámara 5-en-1";
texts[TEXT_MENU_NOKIASTORE] = "Más juegos";
texts[TEXT_MENU_LEGAL] = "Términos de licencia";
texts[TEXT_MENU_BACK] = "Atrás";
texts[TEXT_MENU_HELP] = "Ayuda";
texts[TEXT_MAIN_HELP] = "¡Bienvenido a Pictelligent Cámara 5-en-1!\n\n"
+"Esta aplicación tiene cinco modos diferentes que te permitirán captar atractivas imágenes de formas distintas.\n\n"
+"Retro: después de hacer la foto, puedes aplicarle efectos para que tenga un aspecto antiguo.\n\n"
+"Collage: selecciona un diseño y capta imágenes para crear un collage de tus fotos preferidas.\n\n"
+"Twist: capta una imagen y dale un giro con efectos de retorcido.\n\n"
+"Fusion: selecciona un diseño y capta dos imágenes; nosotros las juntamos.\n\n"
+"Futuro: capta una imagen y dale un aire futurista con nuestros efectos de tecnología digital.";
texts[TEXT_SAVING] = "Guardando…";
texts[TEXT_APPNAME] = "Cámara 5-en-1";
texts[TEXT_ALLRIGHTSRESERVED] = "Reservados todos los derechos";
texts[TEXT_CAMERALOVER] = "Me encantan las cámaras";
texts[TEXT_JPEGGROUP] = "Este software se basa en parte en el trabajo de Independent JPEG Group";
texts[TEXT_FUSION_POPUP] = "NOT USED!";
texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MEMORYFULL] = "Memoria llena";
texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MENU_EULA] = "Términos de licencia";
texts[TEXT_MENU_SELECT] = "Selecc.";
}
    
    /**
     * Load language for Portuguese.
     */

private static void loadPTPT() {
texts[TEXT_RETRO] ="Retro";
texts[TEXT_FUSION] = "Fusão";
texts[TEXT_COLLAGE] ="Colagem";
texts[TEXT_TWIST] = "Pião";
texts[TEXT_FUTURO] = "Futuro";
texts[TEXT_COLLAGE_MODES] = "Efeitos colagem";
texts[TEXT_COLLAGE_MODES_1] = "Efeito 1";
texts[TEXT_COLLAGE_MODES_2] = "Efeito 2";
texts[TEXT_COLLAGE_MODES_3] = "Efeito 3";
texts[TEXT_COLLAGE_MODES_4] = "Efeito 4";
texts[TEXT_COLLAGE_MODES_5] = "Efeito 5";
texts[TEXT_COLLAGE_MODES_6] = "Efeito 6";
texts[TEXT_FUSION_MODES] = "Efeitos fusão";
texts[TEXT_FUSION_MODES_1] = "Efeito 1";
texts[TEXT_FUSION_MODES_2] = "Efeito 2";
texts[TEXT_FUSION_MODES_3] = "Efeito 3";
texts[TEXT_FUSION_MODES_4] = "Efeito 4";
texts[TEXT_FUSION_MODES_5] = "Efeito 5";
texts[TEXT_FUSION_MODES_6] = "Efeito 6";
texts[TEXT_EFFECT_NONE] = "Sem efeito";
texts[TEXT_EFFECT_COLD] = "Frio";
texts[TEXT_EFFECT_WARM] = "Quente";
texts[TEXT_EFFECT_LOMO] = "Lomo";
texts[TEXT_EFFECT_OLD_FILM] = "Filme antigo";
texts[TEXT_EFFECT_BLEACH] = "Bleach";
texts[TEXT_EFFECT_VIGNETTE] = "Vignette";
texts[TEXT_EFFECT_ANTIQUE] = "Antique";
texts[TEXT_TWIST_WAVE] = "Onda";
texts[TEXT_TWIST_TWIRL_LEFT] = "Pião para a esquerda";
texts[TEXT_TWIST_TWIRL_RIGHT] = "Pião para a direita";
texts[TEXT_TWIST_WATERDROP] = "Gota de água";
texts[TEXT_TWIST_SQUEEZE] = "Apertar";
texts[TEXT_TWIST_BUBBLE] = "Bolha";
texts[TEXT_FUTURO_EFFECT1] = "Raios-X";
texts[TEXT_FUTURO_EFFECT2] = "Quadrados";
texts[TEXT_FUTURO_EFFECT3] = "Azul";
texts[TEXT_FUTURO_EFFECT4] = "Espaço";
texts[TEXT_FUTURO_EFFECT5] = "Virtual";
texts[TEXT_FUTURO_EFFECT6] = "Néon";
texts[TEXT_MENU_ABOUT] = "Acerca de";
texts[TEXT_MENU_NOKIASTORE] = "Mais jogos";
texts[TEXT_MENU_LEGAL] = "Termos da licença";
texts[TEXT_MENU_BACK] = "P/ trás";
texts[TEXT_MENU_HELP] = "Ajuda";
texts[TEXT_MAIN_HELP] = "Bem-vindo à Pictelligent Câmara 5-em-1!\n\n"
+"Esta aplicação tem cinco modos diferentes que lhe permitem tirar imagens fantásticas de várias maneiras.\n\n"    
+"Retro: depois de tirar as fotos, pode aplicar efeitos e dar-lhes aquele aspecto antigo.   \n\n"
+"Colagem: seleccione um efeito e tire as fotos para depois criar a sua própria colagem.  \n\n"
+"Torção: tire uma foto e aplique um efeito de pião com efeitos de torção.\n\n"
+"Fusão: seleccione um efeito e tire duas fotos; nós juntamo-las.  \n\n"
+"Futuro: tire uma foto e torne-a futurista com os nossos efeitos de vanguarda.";
texts[TEXT_SAVING] = "A guardar…";
texts[TEXT_APPNAME] = "Câmara 5-em-1";
texts[TEXT_ALLRIGHTSRESERVED] = "Todos os dirfeitos reservados";
texts[TEXT_CAMERALOVER] = "Adoro câmaras fotográficas";
texts[TEXT_JPEGGROUP] = "Este software baseia-se parcialmente no trabalho do Independent JPEG Group";
texts[TEXT_FUSION_POPUP] = "NOT USED!";
texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MEMORYFULL] = "Memória cheia";
texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MENU_EULA] = "Termos da licença";
texts[TEXT_MENU_SELECT] = "Seleccione";
}

/**
 * Load language for Portuguese (Brazil).
 */

private static void loadPTBR() {
texts[TEXT_RETRO] ="Retrô";
texts[TEXT_FUSION] = "Fusão";
texts[TEXT_COLLAGE] ="Colagem";
texts[TEXT_TWIST] = "Twist";
texts[TEXT_FUTURO] = "Futuro";
texts[TEXT_COLLAGE_MODES] = "Layouts de Colagem";
texts[TEXT_COLLAGE_MODES_1] = "Layout 1";
texts[TEXT_COLLAGE_MODES_2] = "Layout 2";
texts[TEXT_COLLAGE_MODES_3] = "Layout 3";
texts[TEXT_COLLAGE_MODES_4] = "Layout 4";
texts[TEXT_COLLAGE_MODES_5] = "Layout 5";
texts[TEXT_COLLAGE_MODES_6] = "Layout 6";
texts[TEXT_FUSION_MODES] = "Layouts de Fusão";
texts[TEXT_FUSION_MODES_1] = "Layout 1";
texts[TEXT_FUSION_MODES_2] = "Layout 2";
texts[TEXT_FUSION_MODES_3] = "Layout 3";
texts[TEXT_FUSION_MODES_4] = "Layout 4";
texts[TEXT_FUSION_MODES_5] = "Layout 5";
texts[TEXT_FUSION_MODES_6] = "Layout 6";
texts[TEXT_EFFECT_NONE] = "Nenhum efeito";
texts[TEXT_EFFECT_COLD] = "Frio";
texts[TEXT_EFFECT_WARM] = "Quente";
texts[TEXT_EFFECT_LOMO] = "Lomo";
texts[TEXT_EFFECT_OLD_FILM] = "Filme antigo";
texts[TEXT_EFFECT_BLEACH] = "Descolorir";
texts[TEXT_EFFECT_VIGNETTE] = "Vinheta";
texts[TEXT_EFFECT_ANTIQUE] = "Antique";
texts[TEXT_TWIST_WAVE] = "Onda";
texts[TEXT_TWIST_TWIRL_LEFT] = "Giro à esquerda";
texts[TEXT_TWIST_TWIRL_RIGHT] = "Giro à direita";
texts[TEXT_TWIST_WATERDROP] = "Gota de água";
texts[TEXT_TWIST_SQUEEZE] = "Comprimir";
texts[TEXT_TWIST_BUBBLE] = "Bolha";
texts[TEXT_FUTURO_EFFECT1] = "Raio X";
texts[TEXT_FUTURO_EFFECT2] = "Quadrados";
texts[TEXT_FUTURO_EFFECT3] = "Azul";
texts[TEXT_FUTURO_EFFECT4] = "Espaço";
texts[TEXT_FUTURO_EFFECT5] = "Virtual";
texts[TEXT_FUTURO_EFFECT6] = "Neon";
texts[TEXT_MENU_ABOUT] = "Sobre a Câmera 5-em-1";
texts[TEXT_MENU_NOKIASTORE] = "Mais jogos";
texts[TEXT_MENU_LEGAL] = "Termos de licença";
texts[TEXT_MENU_BACK] = "Voltar";
texts[TEXT_MENU_HELP] = "Ajuda";
texts[TEXT_MAIN_HELP] = "Bem-vindo à Câmera Pictelligent 5-em-1!\n\n"
+"Este aplicativo tem cinco modos diferentes que permitem que você capture imagens incríveis de diferentes maneiras.\n\n"
+"Retrô: após a captura, você pode aplicar efeitos para dar uma aparência antiga às suas fotos.\n\n"
+"Colagem: selecione um layout e capture imagens para criar sua colagem de fotos favorita.\n\n"
+"Twist: capture uma imagem e altere-a, dando-lhe efeitos de distorção.\n\n"
+"Fusão: selecione um layout e capture duas imagens, com elas, será então criada uma única imagem.\n\n"
+"Futuro: capture uma imagem e lhe dê um ar futurista com nossos efeitos digitais de tecnologia.";
texts[TEXT_SAVING] = "Salvando ...";
texts[TEXT_APPNAME] = "Câmera 5-em-1";
texts[TEXT_ALLRIGHTSRESERVED] = "Todos os direitos reservados";
texts[TEXT_CAMERALOVER] = "Tenho paixão por câmeras";
texts[TEXT_JPEGGROUP] = "Este software é parcialmente baseado no trabalho do Independent JPEG Group";
texts[TEXT_FUSION_POPUP] = "NOT USED!";
texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MEMORYFULL] = "Memória cheia";
texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MENU_EULA] = "Termos de licença";
texts[TEXT_MENU_SELECT] = "Selecionar";
}



    /**
     * Load language for Arabic.
     */

private static void loadAR() {
texts[TEXT_RETRO] ="خلفي";
texts[TEXT_FUSION] = "مزج";
texts[TEXT_COLLAGE] ="ملصق";
texts[TEXT_TWIST] = "ملفوف";
texts[TEXT_FUTURO] = "مستقبلي";
texts[TEXT_COLLAGE_MODES] = "طبقات ملصقة";
texts[TEXT_COLLAGE_MODES_1] = "الطبقة 1";
texts[TEXT_COLLAGE_MODES_2] = "الطبقة 2";
texts[TEXT_COLLAGE_MODES_3] = "الطبقة 3";
texts[TEXT_COLLAGE_MODES_4] = "الطبقة 4";
texts[TEXT_COLLAGE_MODES_5] = "الطبقة 5";
texts[TEXT_COLLAGE_MODES_6] = "الطبقة 6";
texts[TEXT_FUSION_MODES] = "طبقات المزج";
texts[TEXT_FUSION_MODES_1] = "الطبقة 1";
texts[TEXT_FUSION_MODES_2] = "الطبقة 2";
texts[TEXT_FUSION_MODES_3] = "الطبقة 3";
texts[TEXT_FUSION_MODES_4] = "الطبقة 4";
texts[TEXT_FUSION_MODES_5] = "الطبقة 5";
texts[TEXT_FUSION_MODES_6] = "الطبقة 6";
texts[TEXT_EFFECT_NONE] = "بلا تأثير";
texts[TEXT_EFFECT_COLD] = "بارد";
texts[TEXT_EFFECT_WARM] = "دافئ";
texts[TEXT_EFFECT_LOMO] = "ظهير";
texts[TEXT_EFFECT_OLD_FILM] = "فيلم قديم";
texts[TEXT_EFFECT_BLEACH] = "تبييض";
texts[TEXT_EFFECT_VIGNETTE] = "موجز";
texts[TEXT_EFFECT_ANTIQUE] = "قديم";
texts[TEXT_TWIST_WAVE] = "موجات";
texts[TEXT_TWIST_TWIRL_LEFT] = "تدوير لليسار";
texts[TEXT_TWIST_TWIRL_RIGHT] = "تدوير لليمين";
texts[TEXT_TWIST_WATERDROP] = "قطرة ماء";
texts[TEXT_TWIST_SQUEEZE] = "ضغط";
texts[TEXT_TWIST_BUBBLE] = "فقاعات";
texts[TEXT_FUTURO_EFFECT1] = "أشعة إكس";
texts[TEXT_FUTURO_EFFECT2] = "مربعات";
texts[TEXT_FUTURO_EFFECT3] = "أزرق";
texts[TEXT_FUTURO_EFFECT4] = "مسافة";
texts[TEXT_FUTURO_EFFECT5] = "افتراضي";
texts[TEXT_FUTURO_EFFECT6] = "نيون";
texts[TEXT_MENU_ABOUT] = "حول الكاميرا 5 في 1";
texts[TEXT_MENU_NOKIASTORE] = "المزيد من الألعاب";
texts[TEXT_MENU_LEGAL] = "بنود الترخيص";
texts[TEXT_MENU_BACK] = "رجوع";
texts[TEXT_MENU_HELP] = "مساعدة";
texts[TEXT_MAIN_HELP] = "مرحبًا بكم في برنامج  Pictelligent كاميرا 5 في 1!"
+"يتميز هذا البرنامج بخمسة أوضاع مختلفة تسمح لك بالتقاط صور مثيرة بطرق مختلفة.\n\n"
+"خلفي: بعد التقاط الصور يمكنك تطبيق التأثيرات عليها لتضفي على الصورة مظهر قديم.\n\n"
+"ملصق: حدد طبقة والتقط الصور لإنشاء صور ملصقاتك المفضلة.\n\n"
+"لف: التقط صورة وأضفي عليها تأثير التفاف أو دوامة. \n\n"
+"مزج: حدد طبقة والتقط صورتين؛ نقوم بمزجهم.\n\n"
+"مستقبلي: التقط صورة وأجعلها صورة من المستقبل مع تأثيراتنا الرقمية.";
texts[TEXT_SAVING] = "جارٍ الحفظ...";
texts[TEXT_APPNAME] = "كاميرا 5 في 1";
texts[TEXT_ALLRIGHTSRESERVED] = "جميع الحقوق محفوظة";
texts[TEXT_CAMERALOVER] = "أنا من عشاق الكاميرا";
texts[TEXT_JPEGGROUP] = "يعتمد هذا البرنامج جزئيًا على عمل مجموعة JPEG المستقل";
texts[TEXT_FUSION_POPUP] = "NOT USED!";
texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MEMORYFULL] = "الذاكرة ممتلئة";
texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MENU_EULA] = "بنود الترخيص";
texts[TEXT_MENU_SELECT] = "تحديد";
}
    
    /**
     * Load language for Traditional Chinese.
     */
    private static void loadZHTW() {
        texts[TEXT_RETRO] = "復古";
        texts[TEXT_FUSION] = "融合";
        texts[TEXT_COLLAGE] = "拼貼";
        texts[TEXT_TWIST] = "扭曲";
        texts[TEXT_FUTURO] = "未來感";
        texts[TEXT_COLLAGE_MODES] = "拼圖配置";
        texts[TEXT_COLLAGE_MODES_1] = "佈局 1";
        texts[TEXT_COLLAGE_MODES_2] = "佈局 2";
        texts[TEXT_COLLAGE_MODES_3] = "佈局 3";
        texts[TEXT_COLLAGE_MODES_4] = "佈局 4";
        texts[TEXT_COLLAGE_MODES_5] = "佈局 5";
        texts[TEXT_COLLAGE_MODES_6] = "佈局 6";
        texts[TEXT_FUSION_MODES] = "融合佈局";
        texts[TEXT_FUSION_MODES_1] = "佈局 1";
        texts[TEXT_FUSION_MODES_2] = "佈局 2";
        texts[TEXT_FUSION_MODES_3] = "佈局 3";
        texts[TEXT_FUSION_MODES_4] = "佈局 4";
        texts[TEXT_FUSION_MODES_5] = "佈局 5";
        texts[TEXT_FUSION_MODES_6] = "佈局 6";
        texts[TEXT_FUSION_HELP] = "This is the help document of fusion";

        texts[TEXT_EFFECT_NONE] = "無效果";
        texts[TEXT_EFFECT_COLD] = "冷色調";
        texts[TEXT_EFFECT_WARM] = "暖色調";
        texts[TEXT_EFFECT_LOMO] = "Lomo";
        texts[TEXT_EFFECT_OLD_FILM] = "老相片";
        texts[TEXT_EFFECT_BLEACH] = "刷白";
        texts[TEXT_EFFECT_VIGNETTE] = "暈影";
        texts[TEXT_EFFECT_ANTIQUE] = "復古風";

        texts[TEXT_TWIST_WAVE] = "波浪";
        texts[TEXT_TWIST_TWIRL_LEFT] = "左旋轉";
        texts[TEXT_TWIST_TWIRL_RIGHT] = "右旋轉";
        texts[TEXT_TWIST_WATERDROP] = "水滴";
        texts[TEXT_TWIST_SQUEEZE] = "擠壓";
        texts[TEXT_TWIST_BUBBLE] = "泡沫";

        texts[TEXT_FUTURO_EFFECT1] = "X光";
        texts[TEXT_FUTURO_EFFECT2] = "方形";
        texts[TEXT_FUTURO_EFFECT3] = "藍色調";
        texts[TEXT_FUTURO_EFFECT4] = "太空";
        texts[TEXT_FUTURO_EFFECT5] = "虛擬";
        texts[TEXT_FUTURO_EFFECT6] = "霓虹";

        texts[TEXT_MENU_ABOUT] = "關於照像機五合一";
        texts[TEXT_MENU_NOKIASTORE] = "更多遊戲";
        texts[TEXT_MENU_LEGAL] = "法規資訊";
        texts[TEXT_MENU_BACK] = "返回";
        texts[TEXT_MENU_HELP] = "幫助";
        texts[TEXT_MAIN_HELP] = "歡迎使用智圖五合一照相機！\n\n"
                + "本應用有五種模式，可以讓您以不同方式抓拍到激動人心的瞬間。\n\n"
                + "復古：拍攝之後，您可以選擇此效果，使照片有復古感。\n\n"
                + "拼貼：選擇一種拼貼版面，拍攝照片，再創建您最喜歡的拼圖。\n\n"
                + "扭曲：拍攝一張照片，選擇扭曲效果使其有旋風般扭曲感。\n\n"
                + "融合：選擇一種版面，拍攝兩張照片，將它們融合。\n\n"
                + "未來感：拍攝一張照片，可通過我們的數位技術特效使其有未來感。";
        texts[TEXT_SAVING] = "儲存中";
        texts[TEXT_APPNAME] = "照相機五合一";
        texts[TEXT_ALLRIGHTSRESERVED] = "版權所有";
        texts[TEXT_CAMERALOVER] = "我是相機愛好者";
        texts[TEXT_JPEGGROUP] = "獨立的JPEG小組協助完成本軟體";
        texts[TEXT_FUSION_POPUP] = "捕捉拍攝此處";
        
        texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
        texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
        texts[TEXT_MEMORYFULL] = "內存已滿";
        texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
 	    texts[TEXT_MENU_EULA] = "授權條款";
        texts[TEXT_MENU_SELECT] = "選取";

    }
    
    /**
     * Load language for Simplified Chinese.
     */
    private static void loadZHCN() {
        texts[TEXT_RETRO] = "复古";
        texts[TEXT_FUSION] = "融合";
        texts[TEXT_COLLAGE] = "拼贴";
        texts[TEXT_TWIST] = "扭曲";
        texts[TEXT_FUTURO] = "未来感";
        texts[TEXT_COLLAGE_MODES] = "拼图配置";
        texts[TEXT_COLLAGE_MODES_1] = "布局 1";
        texts[TEXT_COLLAGE_MODES_2] = "布局 2";
        texts[TEXT_COLLAGE_MODES_3] = "布局 3";
        texts[TEXT_COLLAGE_MODES_4] = "布局 4";
        texts[TEXT_COLLAGE_MODES_5] = "布局 5";
        texts[TEXT_COLLAGE_MODES_6] = "布局 6";
        texts[TEXT_FUSION_MODES] = "融合布局";
        texts[TEXT_FUSION_MODES_1] = "布局 1";
        texts[TEXT_FUSION_MODES_2] = "布局 2";
        texts[TEXT_FUSION_MODES_3] = "布局 3";
        texts[TEXT_FUSION_MODES_4] = "布局 4";
        texts[TEXT_FUSION_MODES_5] = "布局 5";
        texts[TEXT_FUSION_MODES_6] = "布局 6";
        texts[TEXT_FUSION_HELP] = "This is the help document of fusion";
        
        texts[TEXT_EFFECT_NONE] = "无效果";
        texts[TEXT_EFFECT_COLD] = "冷色调";
        texts[TEXT_EFFECT_WARM] = "暖色调";
        texts[TEXT_EFFECT_LOMO] = "Lomo";
        texts[TEXT_EFFECT_OLD_FILM] = "老相片";
        texts[TEXT_EFFECT_BLEACH] = "刷白";
        texts[TEXT_EFFECT_VIGNETTE] = "晕影";
        texts[TEXT_EFFECT_ANTIQUE] = "复古风";
        
        texts[TEXT_TWIST_WAVE] = "波浪";
        texts[TEXT_TWIST_TWIRL_LEFT] = "左旋转";
        texts[TEXT_TWIST_TWIRL_RIGHT] = "右旋转";
        texts[TEXT_TWIST_WATERDROP] = "水滴";
        texts[TEXT_TWIST_SQUEEZE] = "挤压";
        texts[TEXT_TWIST_BUBBLE] = "泡沫";
        
        texts[TEXT_FUTURO_EFFECT1] = "X光";
        texts[TEXT_FUTURO_EFFECT2] = "方形";
        texts[TEXT_FUTURO_EFFECT3] = "蓝色调";
        texts[TEXT_FUTURO_EFFECT4] = "太空";
        texts[TEXT_FUTURO_EFFECT5] = "虚拟";
        texts[TEXT_FUTURO_EFFECT6] = "霓虹";
        
        texts[TEXT_MENU_ABOUT] = "关于照相机五合一";
        texts[TEXT_MENU_NOKIASTORE] = "更多游戏";
        texts[TEXT_MENU_LEGAL] = "法规信息";
        texts[TEXT_MENU_BACK] = "返回";
        texts[TEXT_MENU_HELP] = "帮助";
        texts[TEXT_MAIN_HELP] = "欢迎使用智图五合一照相机！\n\n"
                + "本应用有五种模式，可以让您以不同方式抓拍到激动人心的瞬间。\n\n"
                + "复古：拍摄之后，您可以选择此效果，使照片有复古感。\n\n"
                + "拼贴：选择一种拼贴版面，拍摄照片，再创建您最喜欢的拼图。\n\n"
                + "扭曲：拍摄一张照片，选择扭曲效果使其有旋风般扭曲感。\n\n"
                + "融合：选择一种版面，拍摄两张照片，将它们融合。\n\n"
                + "未来感：拍摄一张照片，可通过我们的数码技术特效使其有未来感。";
        texts[TEXT_SAVING] = "保存中";
        texts[TEXT_APPNAME] = "照相机五合一";
        texts[TEXT_ALLRIGHTSRESERVED] = "版权所有";
        texts[TEXT_CAMERALOVER] = "我是相机爱好者";
        texts[TEXT_JPEGGROUP] = "独立的JPEG小组协助完成本软件";
        texts[TEXT_FUSION_POPUP] = "捕捉拍摄此处";
        
        texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
        texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
        texts[TEXT_MEMORYFULL] = "内存已满";
        texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
 	    texts[TEXT_MENU_EULA] = "许可条款";
        texts[TEXT_MENU_SELECT] = "选择";

    }
    
    /**
     * Load language for Polish.
     */
 
private static void loadPLPL() {
texts[TEXT_RETRO] ="Retro";
texts[TEXT_FUSION] = "Połącz.";
texts[TEXT_COLLAGE] ="Kolaż";
texts[TEXT_TWIST] = "Skręt";
texts[TEXT_FUTURO] = "Futuro";
texts[TEXT_COLLAGE_MODES] = "Ukł. kolażu";
texts[TEXT_COLLAGE_MODES_1] = "Układ 1";
texts[TEXT_COLLAGE_MODES_2] = "Układ 2";
texts[TEXT_COLLAGE_MODES_3] = "Układ 3";
texts[TEXT_COLLAGE_MODES_4] = "Układ 4";
texts[TEXT_COLLAGE_MODES_5] = "Układ 5";
texts[TEXT_COLLAGE_MODES_6] = "Układ 6";
texts[TEXT_FUSION_MODES] = "Ukł. połącz.";
texts[TEXT_FUSION_MODES_1] = "Układ 1";
texts[TEXT_FUSION_MODES_2] = "Układ 2";
texts[TEXT_FUSION_MODES_3] = "Układ 3";
texts[TEXT_FUSION_MODES_4] = "Układ 4";
texts[TEXT_FUSION_MODES_5] = "Układ 5";
texts[TEXT_FUSION_MODES_6] = "Układ 6";
texts[TEXT_EFFECT_NONE] = "Bez efektów";
texts[TEXT_EFFECT_COLD] = "Zimny";
texts[TEXT_EFFECT_WARM] = "Ciepły";
texts[TEXT_EFFECT_LOMO] = "Lomo";
texts[TEXT_EFFECT_OLD_FILM] = "Stary film";
texts[TEXT_EFFECT_BLEACH] = "Wyblakły";
texts[TEXT_EFFECT_VIGNETTE] = "Winieta";
texts[TEXT_EFFECT_ANTIQUE] = "Antyczny";
texts[TEXT_TWIST_WAVE] = "Fala";
texts[TEXT_TWIST_TWIRL_LEFT] = "Wir lew.";
texts[TEXT_TWIST_TWIRL_RIGHT] = "Wir praw.";
texts[TEXT_TWIST_WATERDROP] = "Kropla wody";
texts[TEXT_TWIST_SQUEEZE] = "Ściśn.";
texts[TEXT_TWIST_BUBBLE] = "Bańka";
texts[TEXT_FUTURO_EFFECT1] = "RTG";
texts[TEXT_FUTURO_EFFECT2] = "Wyrówn.";
texts[TEXT_FUTURO_EFFECT3] = "Nieb.";
texts[TEXT_FUTURO_EFFECT4] = "Przestrz.";
texts[TEXT_FUTURO_EFFECT5] = "Pozorny";
texts[TEXT_FUTURO_EFFECT6] = "Neon";
texts[TEXT_MENU_ABOUT] = "O ap. fot. 5-w-1";
texts[TEXT_MENU_NOKIASTORE] = "Więcej gier";
texts[TEXT_MENU_LEGAL] = "War. licenc.";
texts[TEXT_MENU_BACK] = "Cofn.";
texts[TEXT_MENU_HELP] = "Pomoc";
texts[TEXT_MAIN_HELP] = "Zapraszamy do użytkowania oprogramowania Pictelligent Camera 5-in-1 (Aparat fotograficzny 5-w-1)!\n\n"
+"Aplikacja ta dysponuje 5 różnymi trybami efektów, które pozwalają uchwycić ekscytujące sytuacje w różny sposób.\n\n"
+"Retro: po wykonaniu zdjęcia, możesz zastosować efekty nadające twoim fotografiom wygląd jak z przed lat.\n\n"
+"Kolaż: wybierz żądany układ i wykonaj zdjęcia w sposób pozwalający utworzyć Twój ulubiony fotokolaż.\n\n"
+"Skręt: wykonaj zdjęcie i przepuść je przez „trąbę powietrzną” z efektem skręcenia.\n\n"
+"Połączenie: wybierz układ i wykonaj dwa zdjęcia, a my je dla Ciebie połączymy.\n\n"
+"Futuro: wykonaj zdjęcie i za pomocą naszych cyfrowych efektów specjalnych nadaj mu futurystyczny wygląd.";
texts[TEXT_SAVING] = "Zapis…";
texts[TEXT_APPNAME] = "Ap. fot. 5-w-1";
texts[TEXT_ALLRIGHTSRESERVED] = "Wsz. prawa zast.";
texts[TEXT_CAMERALOVER] = "Jestem fanem fotogr.";
texts[TEXT_JPEGGROUP] = "To oprogramowanie oparte jest w części na pracy niezależnej Grupy JPEG";
texts[TEXT_FUSION_POPUP] = "NOT USED!";
texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MEMORYFULL] = "Pamięć zapełniona";
texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MENU_EULA] = "War. licenc.";
texts[TEXT_MENU_SELECT] = "Wybierz";
}
  
    /**
     * Load language for Russian.
     */

private static void loadRU() {
texts[TEXT_RETRO] ="Ретро";
texts[TEXT_FUSION] = "Слияние";
texts[TEXT_COLLAGE] ="Коллаж";
texts[TEXT_TWIST] = "Кручение";
texts[TEXT_FUTURO] = "Футуро";
texts[TEXT_COLLAGE_MODES] = "Слои коллажа";
texts[TEXT_COLLAGE_MODES_1] = "Слой 1";
texts[TEXT_COLLAGE_MODES_2] = "Слой 2";
texts[TEXT_COLLAGE_MODES_3] = "Слой 3";
texts[TEXT_COLLAGE_MODES_4] = "Слой 4";
texts[TEXT_COLLAGE_MODES_5] = "Слой 5";
texts[TEXT_COLLAGE_MODES_6] = "Слой 6";
texts[TEXT_FUSION_MODES] = "Слои слияния";
texts[TEXT_FUSION_MODES_1] = "Слой 1";
texts[TEXT_FUSION_MODES_2] = "Слой 2";
texts[TEXT_FUSION_MODES_3] = "Слой 3";
texts[TEXT_FUSION_MODES_4] = "Слой 4";
texts[TEXT_FUSION_MODES_5] = "Слой 5";
texts[TEXT_FUSION_MODES_6] = "Слой 6";
texts[TEXT_EFFECT_NONE] = "Без эффекта";
texts[TEXT_EFFECT_COLD] = "Холодный";
texts[TEXT_EFFECT_WARM] = "Теплый";
texts[TEXT_EFFECT_LOMO] = "Ломо";
texts[TEXT_EFFECT_OLD_FILM] = "Старый фильм";
texts[TEXT_EFFECT_BLEACH] = "Белизна";
texts[TEXT_EFFECT_VIGNETTE] = "Виньетка";
texts[TEXT_EFFECT_ANTIQUE] = "Античный";
texts[TEXT_TWIST_WAVE] = "Волна";
texts[TEXT_TWIST_TWIRL_LEFT] = "Кручение влево";
texts[TEXT_TWIST_TWIRL_RIGHT] = "Кручение вправо";
texts[TEXT_TWIST_WATERDROP] = "Капля воды";
texts[TEXT_TWIST_SQUEEZE] = "Оттиск";
texts[TEXT_TWIST_BUBBLE] = "Пузыри";
texts[TEXT_FUTURO_EFFECT1] = "Рентген";
texts[TEXT_FUTURO_EFFECT2] = "Квадраты";
texts[TEXT_FUTURO_EFFECT3] = "Голубой";
texts[TEXT_FUTURO_EFFECT4] = "Космос";
texts[TEXT_FUTURO_EFFECT5] = "Виртуальный";
texts[TEXT_FUTURO_EFFECT6] = "Неон";
texts[TEXT_MENU_ABOUT] = "О Camera 5-in-1";
texts[TEXT_MENU_NOKIASTORE] = "Больше игр";
texts[TEXT_MENU_LEGAL] = "Сроки лицензии";
texts[TEXT_MENU_BACK] = "Назад";
texts[TEXT_MENU_HELP] = "Помощь";
texts[TEXT_MAIN_HELP] = "Добро пожаловать в Pictelligent Camera 5-in-1!\n\n"
+"Это приложение имеет пять разных режимов, которые позволят вам снимать захватывающие фотографии различными способами\n\n"
+"Ретро: после съемки можно придать фотографиям старинный вид.\n\n"
+"Коллаж: выбирайте слой и снимайте фотографии для создания любимого фотоколлажа.\n\n"
+"Кручение: сделайте фотоснимок и придайте ему эффект скручивания.\n\n"
+"Слияние: выбирайте слой и снимайте две фотографии; мы их совместим.\n\n"
+"Футуро: сделайте фотоснимок и придайте ему футуристический вид с помощью наших цифровых эффектов.";
texts[TEXT_SAVING] = "Сохранение…";
texts[TEXT_APPNAME] = "Camera 5-in-1";
texts[TEXT_ALLRIGHTSRESERVED] = "Все права защищены";
texts[TEXT_CAMERALOVER] = "Мне нравится камера";
texts[TEXT_JPEGGROUP] = "Эта программа основана частично на разработках группы Independent JPEG Group";
texts[TEXT_FUSION_POPUP] = "NOT USED!";
texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MEMORYFULL] = "Память переполнена";
texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MENU_EULA] = "Сроки лицензии";
texts[TEXT_MENU_SELECT] = "Выбор";
}



private static void loadTRTR() {
texts[TEXT_RETRO] ="Retro";
texts[TEXT_FUSION] = "Füzyon";
texts[TEXT_COLLAGE] ="Kolaj";
texts[TEXT_TWIST] = "Burma";
texts[TEXT_FUTURO] = "Fütüro";
texts[TEXT_COLLAGE_MODES] = "Kolaj düzeni";
texts[TEXT_COLLAGE_MODES_1] = "Düzen 1";
texts[TEXT_COLLAGE_MODES_2] = "Düzen 2";
texts[TEXT_COLLAGE_MODES_3] = "Düzen 3";
texts[TEXT_COLLAGE_MODES_4] = "Düzen 4";
texts[TEXT_COLLAGE_MODES_5] = "Düzen 5";
texts[TEXT_COLLAGE_MODES_6] = "Düzen 6";
texts[TEXT_FUSION_MODES] = "Füzyon Düzeni";
texts[TEXT_FUSION_MODES_1] = "Düzen 1";
texts[TEXT_FUSION_MODES_2] = "Düzen 2";
texts[TEXT_FUSION_MODES_3] = "Düzen 3";
texts[TEXT_FUSION_MODES_4] = "Düzen 4";
texts[TEXT_FUSION_MODES_5] = "Düzen 5";
texts[TEXT_FUSION_MODES_6] = "Düzen 6";
texts[TEXT_EFFECT_NONE] = "Etkisiz";
texts[TEXT_EFFECT_COLD] = "Soğuk";
texts[TEXT_EFFECT_WARM] = "Sıcak";
texts[TEXT_EFFECT_LOMO] = "Lomo";
texts[TEXT_EFFECT_OLD_FILM] = "Eski film";
texts[TEXT_EFFECT_BLEACH] = "Beyazlatma";
texts[TEXT_EFFECT_VIGNETTE] = "Vinyet";
texts[TEXT_EFFECT_ANTIQUE] = "Antik";
texts[TEXT_TWIST_WAVE] = "Dalga";
texts[TEXT_TWIST_TWIRL_LEFT] = "Sola burma";
texts[TEXT_TWIST_TWIRL_RIGHT] = "Sağa burma";
texts[TEXT_TWIST_WATERDROP] = "Su damlası";
texts[TEXT_TWIST_SQUEEZE] = "Sıkıştırma";
texts[TEXT_TWIST_BUBBLE] = "Kabarcık";
texts[TEXT_FUTURO_EFFECT1] = "X-ışını";
texts[TEXT_FUTURO_EFFECT2] = "Kareler";
texts[TEXT_FUTURO_EFFECT3] = "Mavi";
texts[TEXT_FUTURO_EFFECT4] = "Uzay";
texts[TEXT_FUTURO_EFFECT5] = "Sanal";
texts[TEXT_FUTURO_EFFECT6] = "Neon";
texts[TEXT_MENU_ABOUT] = "Kamera 5-in-1 hak.";
texts[TEXT_MENU_NOKIASTORE] = "Başka oyunlar";
texts[TEXT_MENU_LEGAL] = "Lisans şartları";
texts[TEXT_MENU_BACK] = "Geri";
texts[TEXT_MENU_HELP] = "Yardım";
texts[TEXT_MAIN_HELP] = "Pictelligent Kamera 5-in-1'e hoş geldiniz!\n\n"
+"Bu uygulama farklı biçimlerde heyecanlı görüntüler yakalamanızı sağlayan beş moda sahiptir.\n\n"
+"Retro: çektikten sonra fotoğraflarınıza eski moda görünümü vermek için efektler uygulayabilirsiniz.\n\n"
+"Kolaj: bir düzen seçin ve en sevdiğiniz fotoğrafların kolajını yapmak için fotoğraf çekin.\n\n"
+"Burma: bir fotoğraf seçin ve burarak kasırga efektleri verin.\n\n"
+"Füzyon: bir düzen seçin ve iki fotoğraf seçin; biz onları birleştirelim.\n\n"
+"Fütüro: bir fotoğraf çekin ve dijital teknoloji efektlerimizle ona fütüristik bir hava verin.";
texts[TEXT_SAVING] = "Kaydediliyor…";
texts[TEXT_APPNAME] = "Kamera 5-in-1";
texts[TEXT_ALLRIGHTSRESERVED] = "Tüm haklar saklı";
texts[TEXT_CAMERALOVER] = "Kamera aşığıyım";
texts[TEXT_JPEGGROUP] = "Bu yazılım kısmen Bağımsız JPEG Grubunun çalışmasına dayalıdır";
texts[TEXT_FUSION_POPUP] = "NOT USED!";
texts[TEXT_ABOUT] = "Pictelligent " + texts[TEXT_APPNAME] + "\n" + texts[TEXT_CAMERALOVER];
texts[TEXT_LEGAL_INFO] = "Pictelligent " + texts[TEXT_APPNAME] + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MEMORYFULL] = "Bellek dolu";
texts[TEXT_ABOUT2] = "\n\n(c) Pictelligent Singapore Pte Ltd 2013\n\nwww.pictelligent.com" + "\n\n" + texts[TEXT_ALLRIGHTSRESERVED] + "\n\n" + texts[TEXT_JPEGGROUP];
texts[TEXT_MENU_EULA] = "Lisans şartları";
texts[TEXT_MENU_SELECT] = "Seç";
}



    public static String get(int key) {
        if (texts[key] == null) {
            if (Camera5in1.mDebug)
                DebugUtil.Log("Error reading string ID " + key);
        }
        return texts[key];
    }
}
