package org.games.jmagic.infrastructure;


import java.util.UUID;

/**
 * Identifiable Interface.
 *
 * @author ldavid
 */
public interface IIdentifiable {

    UUID id();

    String name();
}
