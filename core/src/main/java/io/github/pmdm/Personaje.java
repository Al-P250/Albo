package io.github.pmdm;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Personaje{
    Texture protaImg;
    Sprite protaSprite;
    private Vector2 position=new Vector2();
    private Vector2 velocidad = new Vector2();

    Vector2 touchPos;

    boolean suelo;
    private float gravedad;

    public Personaje(String imagen, float inicioX, float inicioY){
        protaImg=new Texture(imagen);
        protaSprite=new Sprite(protaImg);
        getPosition().set(inicioX,inicioY);
        protaSprite.setPosition(inicioX,inicioY);
        touchPos = new Vector2();
        suelo=true;
        setGravedad(1000f);

    }
    public void update(float delta){
        float speed = 500f;

        getVelocidad().y -= getGravedad() * delta;

        getPosition().x += getVelocidad().x*delta;
        getPosition().y += getVelocidad().y*delta;

        if (getPosition().y <= 0) {
            getPosition().y = 0;
            getVelocidad().y = 0;
        }
        protaSprite.setPosition(getPosition().x, getPosition().y);

//        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
//            getVelocidad().x = speed;
//        }
//        else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
//            getVelocidad().x = -speed;
//        }
//        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)&& suelo) {
//            getVelocidad().y = speed;
//            suelo=false;
//        }
//        if (!suelo){
//            getVelocidad().y -= getGravedad() *delta;
//        }
//
//        if (getPosition().y<=0){
//            suelo=true;
//        }
//        if (Gdx.input.isTouched()) {
//            touchPos.set(Gdx.input.getX(), Gdx.input.getY());
//            protaSprite.setCenterX(touchPos.x);
//        }
//
//        getPosition().x += getVelocidad().x * delta;
//        getPosition().y += getVelocidad().y * delta;
//        getPosition().x = Math.max(0, Math.min(getPosition().x, Gdx.graphics.getWidth() - protaSprite.getWidth()));
//        getPosition().y = Math.max(0, Math.min(getPosition().y, Gdx.graphics.getHeight() - protaSprite.getHeight()));
//        protaSprite.setPosition(getPosition().x, getPosition().y);
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
