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
import com.badlogic.gdx.utils.Array;

public class Personaje{
    private Animation<TextureRegion> walkAnimation, jumpAnimation, attackAnimation,idleAnimation;
    Texture protaImg;
    Sprite protaSprite;
    public Vector2 position, velocidad;
    private float stateTime, gravedad;
    private final float FRAME_DURATION = 0.2f;
    boolean suelo, isJumping;

    enum Estado {IDLE, WALK, JUMP, ATTACK}
    private Estado estadoActual;
    private Estado estadoAnterior;

    //ATAQUES
    private boolean isAttacking = false;
    float attackTimer = 0;
    final float ATTACK_DURATION = FRAME_DURATION * 7;
    Rectangle bounds;
    private Rectangle hurtBox, attackBox;
    boolean facingRight = true;

    int saltos=0;
    int numSaltos=2;



    public Personaje(float inicioX, float inicioY){
        position=new Vector2();
        velocidad = new Vector2();

        hurtBox = new Rectangle(inicioX, inicioY, 120, 140);
        attackBox = new Rectangle();

        protaImg = new Texture("SPRITE_SHEET.png");

        int columnas = 10;
        int filas = 11;

        int frameWidth = protaImg.getWidth() / columnas;
        int frameHeight = protaImg.getHeight() / filas;

        TextureRegion[][] regions = TextureRegion.split(protaImg, frameWidth, frameHeight);

        //IDLE (fila 0, 6 frames)
        Array<TextureRegion> idleFrames = new Array<>();
        for (int i = 0; i < 6; i++) {
            idleFrames.add(regions[0][i]);
        }
        idleAnimation = new Animation<>(FRAME_DURATION, idleFrames, Animation.PlayMode.LOOP);

        //WALK (fila 1, 8 frames)
        Array<TextureRegion> walkFrames = new Array<>();
        for (int i = 0; i < 8; i++) {
            walkFrames.add(regions[1][i]);
        }
        walkAnimation = new Animation<>(FRAME_DURATION, walkFrames, Animation.PlayMode.LOOP);

        //JUMP (fila 4, 6)
        Array<TextureRegion> jumpFrames = new Array<>();
        for (int i = 0; i < 6; i++) {
            jumpFrames.add(regions[4][i]);
        }
        jumpAnimation = new Animation<>(FRAME_DURATION, jumpFrames, Animation.PlayMode.NORMAL);

        //ATTACK (fila 3, 7 frames)
        Array<TextureRegion> attackFrames = new Array<>();
        for (int i = 0; i < 7; i++) {
            attackFrames.add(regions[3][i]);
        }
        attackAnimation = new Animation<>(FRAME_DURATION, attackFrames, Animation.PlayMode.NORMAL);

        // Sprite inicial
        protaSprite = new Sprite(idleFrames.first());
        protaSprite.setSize(100,100);
        protaSprite.setPosition(inicioX, inicioY);

        position.set(inicioX, inicioY);

        estadoActual = Estado.IDLE;
        estadoAnterior = Estado.IDLE;

        suelo = true;
        gravedad = 1000f;

        bounds = new Rectangle(inicioX, inicioY, protaSprite.getWidth(),protaSprite.getHeight());
        bounds.setPosition(position.x, position.y);
    }

    public void jump() {
        //se podría cambiar a velocidad.y > 0 para evitar problemas en un futuro
        if (saltos < numSaltos){
            getVelocidad().y = 500f;
            isJumping = true;
            stateTime = 0;
            suelo = false;
            saltos++;
        }
    }
    public void attack() {
        if (!isAttacking) {
            isAttacking = true;
            attackTimer = FRAME_DURATION * 7;
            stateTime = 0;
        }
    }

    public void update(float delta, Array<Rectangle> superficies){
        stateTime += delta;

        getVelocidad().y -= getGravedad() * delta;
        getPosition().x += getVelocidad().x*delta;
        getPosition().y += getVelocidad().y*delta;

        position.x = MathUtils.clamp(position.x, 0, Gdx.graphics.getWidth() - protaSprite.getWidth());
        position.y = MathUtils.clamp(position.y, 0, Gdx.graphics.getHeight() - protaSprite.getHeight());

        if (getPosition().y <= 0) {
            getPosition().y = 0;
            getVelocidad().y = 0;
            suelo = true;
            stateTime = 0;
            saltos=0;
        }

        if (getVelocidad().y <= 0) {
            for (Rectangle rect : superficies) {
                float protaBottom = getPosition().y;
                float rectTop = rect.y + rect.height;

                if (protaBottom >= rectTop - 10 && protaBottom <= rectTop + 10 &&
                    getPosition().x + protaSprite.getWidth() > rect.x &&
                    getPosition().x < rect.x + rect.width) {

                    getPosition().y = rectTop;
                    getVelocidad().y = 0;
                    suelo = true;
                    isJumping = false;
                    saltos = 0;
                    break;
                }
            }
        }
        if (velocidad.x > 0) facingRight = true;
        else if (velocidad.x < 0) facingRight = false;

        if (isAttacking) {
            attackBox.width = 60;
            attackBox.height = 80;
            attackBox.y = position.y + 20;

            if (facingRight) {
                attackBox.x = position.x + protaSprite.getWidth();
            } else {
                attackBox.x = position.x - attackBox.width;
            }

            attackTimer -= delta;
            if (attackTimer <= 0) {
                isAttacking = false;
            }
        } else {
            attackBox.set(0, 0, 0, 0);
        }

        estadoAnterior = estadoActual;

        if (isAttacking()) {
            estadoActual = Estado.ATTACK;
        }
        else if (Math.abs(velocidad.y) > 1f) {
            estadoActual = Estado.JUMP;
        }
        else if (Math.abs(velocidad.x) > 5f) {
            estadoActual = Estado.WALK;
        }
        else {
            estadoActual = Estado.IDLE;
        }

        if (estadoActual != estadoAnterior) {
            stateTime = 0;
        }

        TextureRegion currentFrame = switch (estadoActual) {
            case WALK   -> walkAnimation.getKeyFrame(stateTime);
            case JUMP   -> jumpAnimation.getKeyFrame(stateTime);
            case ATTACK -> attackAnimation.getKeyFrame(stateTime);
            default     -> idleAnimation.getKeyFrame(stateTime);
        };

        protaSprite.setRegion(currentFrame);
        protaSprite.setFlip(!facingRight, false);
        protaSprite.setPosition(position.x, position.y);

        bounds.setPosition(position.x, position.y);
        hurtBox.setPosition(position.x, position.y);

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
