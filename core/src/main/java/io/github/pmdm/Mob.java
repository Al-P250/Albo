package io.github.pmdm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Mob extends Entidad {
    Sprite mobSprite;
    private Animation<TextureRegion> walkAnimation, deathAnimation;
    Texture deathImg, walkImg;
    private Rectangle bounds;
    private boolean isDead = false;

    private boolean eliminar=false;
    public Mob(float x, float y, String spriteSheetPath, int frameCount) {
        super(spriteSheetPath, frameCount, 0.1f);
        sprite.setPosition(x, y);

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

        setBounds(new Rectangle(x, y,120,200));
    }

    public void update(float deltaTime, Vector2 posPersonaje) {
        stateTime += deltaTime;

        if (!isDead) {
            Vector2 direccion = new Vector2(posPersonaje.x - position.x, posPersonaje.y - position.y);
            direccion.nor();

            position.y= 290;

            boolean flip = (direccion.x<0);
            TextureRegion currentFrame = walkAnimation.getKeyFrame(stateTime);

            if ((flip && !currentFrame.isFlipX()) || (!flip && currentFrame.isFlipX())) {
                currentFrame.flip(true,false);
            }
            if (flip){
                position.x-= velocidad.x*deltaTime;
            }else {
                position.x += velocidad.x * deltaTime;
            }
            position.x = MathUtils.clamp(position.x, 600,1700);

            mobSprite.setRegion(currentFrame);
        } else {
            mobSprite.setRegion(deathAnimation.getKeyFrame(stateTime));
            if (deathAnimation.isAnimationFinished(stateTime)) {
                eliminar = true;
            }
        }

        mobSprite.setSize(160, 320);
        mobSprite.setPosition(position.x, position.y);
        bounds.setPosition(position.x, position.y);
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
}
