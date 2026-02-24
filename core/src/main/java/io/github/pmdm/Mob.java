package io.github.pmdm;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Mob extends Entidad {
    Sprite mobSprite;
    private Animation<TextureRegion> walkAnimation, deathAnimation;
    Texture deathImg, walkImg;
    private Rectangle bounds;
    private boolean isDead = false;
    public Mob(float x, float y, String spriteSheetPath, int frameCount) {
        super(spriteSheetPath, frameCount, 0.1f);
        sprite.setPosition(x, y);

        setBounds(new Rectangle((int) x, (int) y,spriteSheet.getWidth(),spriteSheet.getHeight()));

        walkImg = new Texture("SkeletonWalk.png");
        int walkFrameWidth = walkImg.getWidth() / frameCount;
        int walkFrameHeight = walkImg.getHeight();

        TextureRegion[][] tmpWalk = TextureRegion.split(walkImg,walkFrameWidth,walkFrameHeight);

        walkAnimation = new Animation<>(FRAME_DURATION,tmpWalk[0]);
        walkAnimation.setPlayMode(Animation.PlayMode.LOOP);

        mobSprite = new Sprite(walkImg);

        deathImg = new Texture("Idle_KG_2.png");
        int deathFrameWidth = deathImg.getWidth() / frameCount;
        int deathFrameHeight = deathImg.getHeight();

        TextureRegion[][] tmpDeath = TextureRegion.split(deathImg,deathFrameWidth,deathFrameHeight);

        deathAnimation = new Animation<>(FRAME_DURATION, tmpDeath[0]);
        deathAnimation.setPlayMode(Animation.PlayMode.NORMAL);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        TextureRegion currentFrame;

        if (!isDead) {
            currentFrame = walkAnimation.getKeyFrame(stateTime);
        } else {
            currentFrame = deathAnimation.getKeyFrame(stateTime);
        }
        mobSprite.setRegion(currentFrame);
        mobSprite.setPosition(position.x, position.y);
        bounds.setPosition(position.x, position.y);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        sprite.setSize(160, 320);
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
        isDead = dead;
    }
}
