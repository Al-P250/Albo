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
    private Animation<TextureRegion> walkAnimation, jumpAnimation, attackAnimation,idleAnimation, hurtAnimation, deadAnimation;
    Texture protaImg;
    Sprite protaSprite;
    public Vector2 position, velocidad;
    private float stateTime, gravedad;
    private final float FRAME_DURATION = 0.2f;
    boolean suelo, isJumping;


    enum Estado {IDLE, WALK, JUMP, ATTACK, HURT, DEAD}
    private Estado estadoActual;
    private Estado estadoAnterior;

    //ATAQUES
    private boolean isAttacking = false;
    private boolean isDead = false;
    private boolean isHurt = false;
    float attackTimer = 0;
    final float ATTACK_DURATION = FRAME_DURATION * 7;
    Rectangle bounds;
    private Rectangle hurtBox, attackBox;
    boolean facingRight = true;

    int saltos=0;
    int numSaltos=2;

    //DAÑO RECIBIDO
    private float hurtTimer = 0;
    private final float HURT_DURATION = FRAME_DURATION * 4;
    private int vidas = 3;


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

        //HURT (fila 7, 4 frames)
        Array<TextureRegion> hurtFrames = new Array<>();
        for (int i = 0; i < 4; i++) {
            hurtFrames.add(regions[7][i]);
        }
        hurtAnimation = new Animation<>(FRAME_DURATION, hurtFrames, Animation.PlayMode.NORMAL);

        //DEAD (fila 6, 10 frames)
        Array<TextureRegion> deadFrames = new Array<>();
        for (int i = 0; i < 10; i++) {
            deadFrames.add(regions[6][i]);
        }
        deadAnimation = new Animation<>(FRAME_DURATION, deadFrames, Animation.PlayMode.NORMAL);

        // Sprite inicial
        protaSprite = new Sprite(idleFrames.first());
        protaSprite.setSize(100,100);
        protaSprite.setPosition(inicioX, inicioY);

        position.set(inicioX, inicioY);

        estadoActual = Estado.IDLE;
        estadoAnterior = Estado.IDLE;

        suelo = true;
        gravedad = 1000f;

        bounds = new Rectangle(inicioX+30, inicioY, 40,70);
        bounds.setPosition(position.x, position.y);
    }

    public void jump() {
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
    public void quitarVida(int cantidad) {
        if (!isHurt) {
            setVidas(getVidas() - cantidad);
            setHurt(true);
            hurtTimer = HURT_DURATION;
            stateTime = 0;

            if (facingRight) velocidad.x = -200;
            else velocidad.x = 200;
        }
    }



    public void update(float delta, Array<Rectangle> superficies){
        stateTime += delta;

        velocidad.y -= gravedad * delta;
        position.x += velocidad.x * delta;
        bounds.setPosition(position.x, position.y);

        for (Rectangle rect : superficies) {
            if (bounds.overlaps(rect)) {

                if (velocidad.x > 0) {
                    position.x = rect.x - bounds.width;
                }
                else if (velocidad.x < 0) {
                    position.x = rect.x + rect.width;
                }

                velocidad.x = 0;
                bounds.setPosition(position.x, position.y);
            }
        }

        position.y += velocidad.y * delta;
        bounds.setPosition(position.x, position.y);

        suelo = false;

        for (Rectangle rect : superficies) {
            if (bounds.overlaps(rect)) {

                if (velocidad.y > 0) {
                    position.y = rect.y - bounds.height;
                }
                else if (velocidad.y < 0) {
                    position.y = rect.y + rect.height;
                    suelo = true;
                    saltos = 0;
                }

                velocidad.y = 0;
                bounds.setPosition(position.x, position.y);
            }
        }

        position.x = MathUtils.clamp(position.x, 0, Gdx.graphics.getWidth() - protaSprite.getWidth());
        position.y = MathUtils.clamp(position.y, 0, Gdx.graphics.getHeight() - protaSprite.getHeight());

        if (getPosition().y <= 0) {
            getPosition().y = 0;
            getVelocidad().y = 0;
            suelo = true;
            stateTime = 0;
            saltos=0;
        }

        if (velocidad.x > 0) facingRight = true;
        else if (velocidad.x < 0) facingRight = false;

        if (!isDead){

        }

        if (isAttacking) {
            attackTimer -= delta;
            if (attackTimer <= 0) isAttacking = false;
        }

        if (isHurt) {
            hurtTimer -= delta;
            if (hurtTimer <= 0) isHurt = false;
        }


        if (isAttacking) {
            attackBox.width = 60;
            attackBox.height = 80;
            attackBox.y = position.y;

            if (facingRight) {
                attackBox.x = position.x + protaSprite.getWidth()-60;
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

        if (!isDead) {
            if (isAttacking()) {
                estadoActual = Estado.ATTACK;
            } else if (Math.abs(velocidad.y) > 1f) {
                estadoActual = Estado.JUMP;
            } else if (Math.abs(velocidad.x) > 5f) {
                estadoActual = Estado.WALK;
            } else if (isHurt) {
                estadoActual = Estado.HURT;
            } else {
                estadoActual = Estado.IDLE;
            }
        } else {
            estadoActual = Estado.DEAD;
            velocidad.x = 0;
        }

        if (estadoActual != estadoAnterior) {
            stateTime = 0;
        }

        TextureRegion currentFrame = switch (estadoActual) {
            case WALK   -> walkAnimation.getKeyFrame(stateTime);
            case JUMP   -> jumpAnimation.getKeyFrame(stateTime);
            case ATTACK -> attackAnimation.getKeyFrame(stateTime);
            case HURT -> hurtAnimation.getKeyFrame(stateTime);
            case DEAD -> deadAnimation.getKeyFrame(stateTime);
            default     -> idleAnimation.getKeyFrame(stateTime);
        };

        protaSprite.setRegion(currentFrame);
        protaSprite.setFlip(!facingRight, false);
        if (!facingRight){
            protaSprite.setPosition(position.x-25, position.y-15);

        }else {
            protaSprite.setPosition(position.x - 35, position.y - 15);
        }

        bounds.setPosition(position.x, position.y);
        hurtBox.setPosition(position.x, position.y);

    }
    public boolean isHurt() {
        return isHurt;
    }

    public void setHurt(boolean hurt) {
        isHurt = hurt;
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

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
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
}
