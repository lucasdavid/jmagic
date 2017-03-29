package hearthstone.core.contracts;

/**
 * Damageable Interface.
 *
 * @author ldavid
 */
public interface IDamageable extends ITargetable {

    IDamageable takeDamage(int damage);

    int getLife();

    int getMaxLife();
}
