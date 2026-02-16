package io.github.pmdm;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Mob {
    private Array<TextureRegion> frames; // Array para almacenar los frames de la animación
    private Texture spriteSheet; // Hoja de sprites
    private Sprite sprite; // Sprite
    private Vector2 position; // Posición del mob
    private Vector2 velocity; // Velocidad del mob
    private float stateTime; // Tiempo para animación
    private final float FRAME_DURATION = 0.1f; // Duración de cada frame

    public Mob(float x, float y, String spriteSheetPath, int frameCount) {
        this.spriteSheet = new Texture(spriteSheetPath); // Cargar la hoja de sprites
        this.frames = new Array<>();

        int frameWidth = spriteSheet.getWidth() / frameCount;
        int frameHeight = spriteSheet.getHeight();

        for (int i = 0; i < frameCount; i++) {
            TextureRegion frame = new TextureRegion(spriteSheet, i * frameWidth, 0, frameWidth, frameHeight);
            frames.add(frame);
        }

        this.position = new Vector2(x, y); // Inicializar posición
        this.velocity = new Vector2(0, 0); // Velocidad inicial
        this.sprite = new Sprite(frames.get(0)); // Iniciar con el primer frame
        sprite.setPosition(x, y);
        stateTime = 0f; // Inicializar tiempo de estado
    }

    public void update(float deltaTime) {
        // Actualizar la posición del mob
        position.add(velocity.cpy().scl(deltaTime));
        sprite.setPosition(position.x, position.y); // Actualizar posición del sprite

        // Actualizar la animación
        stateTime += deltaTime;
        int currentFrameIndex = (int) (stateTime / FRAME_DURATION) % frames.size;
        sprite.setRegion(frames.get(currentFrameIndex)); // Cambiar al frame actual
    }

    public void setVelocity(float x, float y) {
        this.velocity.set(x, y); // Establecer la velocidad
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch); // Dibujar el sprite en el batch
    }

    public void dispose() {
        spriteSheet.dispose(); // Liberar la hoja de sprites
    }
}
