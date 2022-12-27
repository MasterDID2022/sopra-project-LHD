package fr.univtln.lhd.facade;

import java.util.List;

public interface Observer {
    void udpate(List<EventChange<?>> changes);
}
