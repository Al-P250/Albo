package io.github.pmdm;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Mob extends Entidad {
    Sprite mobSprite;
    private Animation<TextureRegion> walkAnimation, deathAnimation, attackAnimation;
    Texture deathImg, walkImg, attackImg;
    private Rectangle bounds;
    private boolean isDead = false;
    private boolean eliminar=false;
    //ATAQUES
    private boolean isAttacking = false;
    float attackTimer = 0;
    final float ATTACK_DURATION = FRAME_DURATION * 7;
    private Rectangle hurtBox;
    private Rectangle attackBox;

    public Mob(float x, float y, String spriteSheetPath, int frameCount) {
        super(spriteSheetPath, frameCount, 0.1f);
        sprite.setPosition(x, y);

        hurtBox = new Rectangle();
        setAttackBox(new Rectangle());

        walkImg = new Texture("SkeletonWalk.png");
        int walkFrameWidth = walkImg.getWidth() / frameCount;
        int walkFrameHeight = walkImg.getHeight();
        TextureRegion[][] tmpWalk = TextureRegion.split(walkImg,walkFrameWidth,walkFrameHeight);
        walkAnimation = new Animation<>(FRAME_DURATION,tmpWalk[0]);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        mobSprite = new Sprite();
        deathImg = new Texture("SkeletonDead.png");
        int deathFrameWidth = deathImg.getWidth() / 15;
        int deathFrameHeight = deathImg.getHeight();
        TextureRegion[][] tmpDeath = TextureRegion.split(deathImg,deathFrameWidth,deathFrameHeight);
        deathAnimation = new Animation<>(FRAME_DURATION, tmpDeath[0]);
        deathAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        attackImg = new Texture("SkeletonAttack.png");
        int attackFrameWidth = attackImg.getWidth() / 15;
        int attackFrameHeight = attackImg.getHeight();
        TextureRegion[][] tmpAttack = TextureRegion.split(attackImg,attackFrameWidth,attackFrameHeight);
        attackAnimation = new Animation<>(FRAME_DURATION, tmpAttack[0]);
        attackAnimation.setPlayMode(Animation.PlayMode.NORMAL);

        setBounds(new Rectangle(x, y,120,200));
    }

    public void update(float deltaTime, Vector2 posPersonaje) {
        stateTime += deltaTime;

        if (!isDead) {
            float distancia = position.dst(posPersonaje);
            float rangoAtaque = 60f;

            if (distancia < rangoAtaque && !isAttacking()) {
                attack();
            }
            if (isAttacking()) {
                attackTimer -= deltaTime;
                TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime);

                getAttackBox().width = 60;
                getAttackBox().height = 80;
                getAttackBox().y = position.y + (mobSprite.getHeight() / 4);

                boolean flip = (posPersonaje.x < position.x);
                if (flip) {
                    getAttackBox().x = position.x - getAttackBox().width;
                } else {
                    getAttackBox().x = position.x + mobSprite.getWidth();
                }

                if ((flip && !currentFrame.isFlipX()) || (!flip && currentFrame.isFlipX())) {
                    currentFrame.flip(true,false);
                }

                mobSprite.setRegion(currentFrame);

                if (attackAnimation.isAnimationFinished(stateTime)) {
                    setAttacking(false);
                    stateTime = 0;
                }
            } else {
                Vector2 direccion = new Vector2(posPersonaje.x - position.x, posPersonaje.y - position.y);
                direccion.nor();

                position.x += direccion.x * velocidad.x * deltaTime;
                position.y += direccion.y * velocidad.y * deltaTime;
            position.y= 300;

                TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime);
                boolean flip = (direccion.x < 0);
                if ((flip && !currentFrame.isFlipX()) || (!flip && currentFrame.isFlipX())) {
                    currentFrame.flip(true, false);
                }
                mobSprite.setRegion(currentFrame);
            }
        } else {
            mobSprite.setRegion(deathAnimation.getKeyFrame(stateTime));
            if (deathAnimation.isAnimationFinished(stateTime)) {
                eliminar = true;
            }
        }

        mobSprite.setPosition(position.x, position.y);
        bounds.setPosition(position.x, position.y);
    }
    public void attack() {
        if (!isAttacking()) {
            setAttacking(true);
            attackTimer = FRAME_DURATION * 7;
            stateTime = 0;
        }
    }

    public boolean checkAttackHit(Rectangle characterBounds) {
        if (isAttacking() && getAttackBox().overlaps(characterBounds)) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(SpriteBatch batch) {
        mobSprite.draw(batch);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        if (!this.isDead && dead) {
            this.isDead = true;
            this.stateTime = 0;
            this.velocidad.set(0, 0);
        }
    }
    public boolean shouldRemove() {
        return eliminar;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }

    public Rectangle getAttackBox() {
        return attackBox;
    }

    public void setAttackBox(Rectangle attackBox) {
        this.attackBox = attackBox;
    }
}
