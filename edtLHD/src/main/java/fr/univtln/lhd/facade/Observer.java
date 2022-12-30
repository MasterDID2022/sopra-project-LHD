package fr.univtln.lhd.facade;

public interface Observer {
    void update(EventChange<?> change);
}
