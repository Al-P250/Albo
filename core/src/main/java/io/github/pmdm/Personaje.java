package io.github.pmdm;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Personaje{
    Texture protaImg;

    Sprite protaSprite;

    Vector2 velocidad=new Vector2();
    float vel=500;
    public Personaje(String imagen, float inicioX, float inicioY){
        protaImg=new Texture(imagen);
        protaSprite=new Sprite(protaImg);
        protaSprite.setPosition(inicioX,inicioY);
    }

    public void update(float delta){
        velocidad.setZero();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            velocidad.x=-1;
        } if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            velocidad.x =1;
        }

        velocidad.nor().scl(vel * delta);
        protaSprite.translate(velocidad.x,velocidad.y);


        float x=Math.max(0, Math.min(protaSprite.getX(), Gdx.graphics.getWidth() - protaSprite.getWidth()));
        float y=Math.max(0, Math.min(protaSprite.getY(), Gdx.graphics.getHeight()- protaSprite.getHeight()));

        protaSprite.setPosition(x,y);
    }
    public void draw(SpriteBatch batch) {
        protaSprite.draw(batch);

    }

    public void dispose(){
        protaImg.dispose();
    }
}
