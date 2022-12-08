module fr.univtln.lhd {
    requires javafx.controls;

    requires com.calendarfx.view;

    requires java.logging;

    requires static lombok;
    requires java.naming;

    exports fr.univtln.lhd.view;
    exports fr.univtln.lhd.model.entitys;
}