package io.github.pmdm;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Personaje{
    Texture jumpImg;
    TextureRegion jumpFrame;
    Texture protaImg;
    Sprite protaSprite;
    private Vector2 position=new Vector2();
    private Vector2 velocidad = new Vector2();
    private Array<TextureRegion> frames;
    private float stateTime;
    private final float FRAME_DURATION = 0.2f;
    Vector2 touchPos;
    boolean suelo;
    boolean isJumping;
    private float gravedad;
    Rectangle bounds;
    public Personaje(String imagen, float inicioX, float inicioY, int frameCount){
        this.protaImg=new Texture(imagen);
        this.frames = new Array<>();

        int frameWidth = protaImg.getWidth() / frameCount;
        int frameHeight = protaImg.getHeight();

        for (int i = 0; i < frameCount; i++) {
            TextureRegion frame = new TextureRegion(protaImg, i * frameWidth, 0, frameWidth, frameHeight);
            frames.add(frame);
        }


        protaSprite=new Sprite(frames.get(0));
        protaSprite.setPosition(inicioX,inicioY);
        getPosition().set(inicioX,inicioY);
        protaSprite.setPosition(inicioX,inicioY);
        touchPos = new Vector2();
        suelo=true;
        setGravedad(1000f);
        protaSprite.setSize(160,160);

        jumpImg = new Texture("Jump_KG_2.png");
        jumpFrame = new TextureRegion(jumpImg);

        bounds = new Rectangle(inicioX, inicioY,100,120);

    }

    public void jump() {
        //se podría cambiar a velocidad.y > 0 para evitar problemas en un futuro
        if (suelo) {
            getVelocidad().y = 500f;
            isJumping = true;
            stateTime = 0;
            suelo = false;
        }
    }
    public void update(float delta){

        getVelocidad().y -= getGravedad() * delta;
        getPosition().x += getVelocidad().x*delta;
        getPosition().y += getVelocidad().y*delta;

        if (getPosition().y <= 0) {
            getPosition().y = 0;
            getVelocidad().y = 0;
            suelo = true;
            isJumping = false;
        }

        stateTime += delta;
        stateTime += delta;

        if (!suelo) {
            int frameWidth = protaImg.getWidth() / 5;
            int frameHeight = protaImg.getHeight();
            for (int i = 0; i < 5; i++) {
                TextureRegion frame = new TextureRegion(protaImg, i * frameWidth, 0, frameWidth, frameHeight);
                frames.add(frame);
            }
            protaSprite.setRegion(jumpFrame);
        } else {
            int currentFrameIndex = (int) (stateTime / FRAME_DURATION) % frames.size;
            protaSprite.setRegion(frames.get(currentFrameIndex));
        }

    }
    public Rectangle getBounds() {
        return bounds;
    }
    public void draw(SpriteBatch batch) {
        protaSprite.draw(batch);
    }
    public void dispose(){
        protaImg.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(Vector2 velocidad) {
        this.velocidad = velocidad;
    }

    public float getGravedad() {
        return gravedad;
    }

    public void setGravedad(float gravedad) {
        this.gravedad = gravedad;
    }
}
