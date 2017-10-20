package cs6250.benchmarkingsuite.imageprocessing.pipeline;

import java.util.ArrayList;

import cs6250.benchmarkingsuite.imageprocessing.effects.Effect;

/**
 * Created by farzon on 10/14/17.
 */

public interface IPipeline {
    public ArrayList<Effect> getEffects();

    public void addEffect(EffectTask effect);

    public void clearEffects();

    public void start();

    public void stop();
}
