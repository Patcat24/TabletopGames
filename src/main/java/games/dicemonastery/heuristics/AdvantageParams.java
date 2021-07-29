package games.dicemonastery.heuristics;

import evaluation.TunableParameters;
import players.heuristics.ActionAdvantageHeuristic;

public class AdvantageParams extends TunableParameters {

    public double rndWeight = 0.5;

    public AdvantageParams() {
        addTunableParameter("rndWeight", 0.5);
    }

    @Override
    protected AdvantageParams _copy() {
        return new AdvantageParams();
    }

    @Override
    protected boolean _equals(Object o) {
        if (o instanceof AdvantageParams) {
            return ((AdvantageParams) o).rndWeight == rndWeight;
        }
        return false;
    }

    @Override
    public ActionAdvantageHeuristic instantiate() {
        return new ActionAdvantageHeuristic();
    }

    @Override
    public void _reset() {
        rndWeight = (double) getParameterValue("rndWeight");
    }
}
