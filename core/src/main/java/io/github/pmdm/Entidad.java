package io.github.pmdm;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Entidad {
    protected Array<TextureRegion> frames;
    protected Texture spriteSheet;
    protected Sprite sprite;
    protected Vector2 position;
    protected Vector2 velocidad;
    protected float stateTime;
    protected final float FRAME_DURATION;

    public Entidad(String spriteSheetPath, int frameCount, float frameDuration) {
        this.spriteSheet = new Texture(spriteSheetPath);
        this.frames = new Array<>();
        this.FRAME_DURATION = frameDuration;
        int frameWidth = spriteSheet.getWidth() / frameCount;
        int frameHeight = spriteSheet.getHeight();

        for (int i = 0; i < frameCount; i++) {
            TextureRegion frame = new TextureRegion(spriteSheet, i * frameWidth, 0, frameWidth, frameHeight);
            frames.add(frame);
        }

        this.position = new Vector2();
        this.velocidad = new Vector2();
        velocidad.x = -100;
        this.sprite = new Sprite(frames.get(0));
        this.stateTime = 0f;
    }

    public void update(float deltaTime) {
        position.add(velocidad.cpy().scl(deltaTime));
        sprite.setPosition(position.x, position.y);
        stateTime += deltaTime;
        int currentFrameIndex = (int) (stateTime / FRAME_DURATION) % frames.size;
        sprite.setRegion(frames.get(currentFrameIndex));
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void dispose() {
        spriteSheet.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setVelocity(float x, float y) {
        this.velocidad.set(x, y);
    }
}
