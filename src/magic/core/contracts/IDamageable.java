package magic.core.contracts;

/**
 * Damageable Interface.
 *
 * @author ldavid
 */
public interface IDamageable extends ITargetable {

    IDamageable takeDamage(int damage);

    int life();

    int effectiveLife();

    int effectiveMaxLife();

    int maxLife();

    boolean isAlive();
}
