package classes.agents;

public interface AgentInterface {
	void actuate();
	void rateLastActuation();
	void calculateNewActuation();
}