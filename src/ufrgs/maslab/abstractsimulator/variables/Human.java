package ufrgs.maslab.abstractsimulator.variables;

import java.util.ArrayList;
import java.util.Arrays;

import ufrgs.maslab.abstractsimulator.constants.MessageType;
import ufrgs.maslab.abstractsimulator.exception.SimulatorException;
import ufrgs.maslab.abstractsimulator.log.HumanLogger;
import ufrgs.maslab.abstractsimulator.mailbox.message.Message;
import ufrgs.maslab.abstractsimulator.util.Transmitter;

public class Human extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8750788016563339824L;
	
	public static Integer counterOmega = 0;
	
	private String exceptionFileName = "exception.properties";
	
	private transient ArrayList<String> attributes = new ArrayList<String>(Arrays.asList("Physical","Social","Mental"));
	
	private ArrayList<String> priority = new ArrayList<String>();
	
	/**
	 * laziness ratio of the agent
	 */
	private Double omega;
	
	private Double lazyRatio = Transmitter.getDoubleConfigParameter("config.properties", "lazy.ratio");
	private Double commitedRatio = Transmitter.getDoubleConfigParameter("config.properties", "commited.ratio");
	private Double totalAgents = Transmitter.getDoubleConfigParameter("config.properties", "config.variables");
	
	private Double lazy = Transmitter.getDoubleConfigParameter("config.properties", "agent.lazy");
	private Double commited = Transmitter.getDoubleConfigParameter("config.properties", "agent.commited");
	private Double balanced = Transmitter.getDoubleConfigParameter("config.properties", "agent.balanced");
	
	/**
	 * physical attributes
	 */
	private int strength = 1;
	private int dexterity = 1;
	private int stamina = 1;
	
	/**
	 * social attributes
	 */
	private int charisma = 1;
	private int appearance = 1;
	private int leadership = 1;
	
	/**
	 * mental attributes
	 */
	private int intelligence = 1;
	private int reasoning = 1;
	private int perception = 1;
	
	
	/**
	 * other attributes
	 */
	private int will = 5;
	private int hp = 7;
	
	/**
	 * <ul>
	 * <li>ear of the human</li>
	 * <li>all messages comes here first</li>
	 * </ul>
	 */
	private ArrayList<Message> ear = new ArrayList<Message>();
	
	
	/**
	 * <ul>
	 * <li>default constructor</li>
	 * <li>generates a random human</li>
	 * </ul>
	 */
	public Human(){
		super();
		try {
			this.randomHuman();
		} catch (SimulatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * defines random priority and random attribute values to the human agent
	 * 
	 * @throws SimulatorException
	 */
	private void randomHuman() throws SimulatorException{
		//defines priority of the attributes
		this.definePriotity();
		//define attribute values
		this.defineAttributeValues();
		
		Double lazyCounter = (this.totalAgents * this.lazyRatio);
		Double commitedCounter = (this.totalAgents * this.commitedRatio);
		
		if(counterOmega < lazyCounter.intValue())
		{
			this.setOmega(this.lazy);
		}else if(counterOmega < commitedCounter.intValue())
		{
			this.setOmega(this.commited);
		}else{
			this.setOmega(this.balanced);
		}
		
		counterOmega++;
	}
	
	/**
	 * assign attribute values
	 * @throws SimulatorException
	 */
	private void defineAttributeValues() throws SimulatorException{
		if(this.priority.isEmpty())
			throw new SimulatorException(Transmitter.getProperty(this.exceptionFileName, "exception.no.priority"));
		
		int[] attributes1 = this.rollAttributesValue(7);
		int[] attributes2 = this.rollAttributesValue(5);
		int[] attributes3 = this.rollAttributesValue(3);
		attributes1[0]++;
		attributes1[1]++;
		attributes1[2]++;
		attributes2[0]++;
		attributes2[1]++;
		attributes2[2]++;
		attributes3[0]++;
		attributes3[1]++;
		attributes3[2]++;
		switch(this.getPriority().get(0))
		{
			case "Physical":
				this.setStrength(attributes1[0]);
				this.setDexterity(attributes1[1]);
				this.setStamina(attributes1[2]);
				break;
			case "Social":
				this.setCharisma(attributes1[0]);
				this.setAppearance(attributes1[1]);
				this.setLeadership(attributes1[2]);
				break;
			case "Mental":
				this.setIntelligence(attributes1[0]);
				this.setReasoning(attributes1[1]);
				this.setPerception(attributes1[2]);
				break;
		}
		switch(this.getPriority().get(1))
		{
			case "Physical":
				this.setStrength(attributes2[0]);
				this.setDexterity(attributes2[1]);
				this.setStamina(attributes2[2]);
				break;
			case "Social":
				this.setCharisma(attributes2[0]);
				this.setAppearance(attributes2[1]);
				this.setLeadership(attributes2[2]);
				break;
			case "Mental":
				this.setIntelligence(attributes2[0]);
				this.setReasoning(attributes2[1]);
				this.setPerception(attributes2[2]);
				break;
		}
		switch(this.getPriority().get(2))
		{
			case "Physical":
				this.setStrength(attributes3[0]);
				this.setDexterity(attributes3[1]);
				this.setStamina(attributes3[2]);
				break;
			case "Social":
				this.setCharisma(attributes3[0]);
				this.setAppearance(attributes3[1]);
				this.setLeadership(attributes3[2]);
				break;
			case "Mental":
				this.setIntelligence(attributes3[0]);
				this.setReasoning(attributes3[1]);
				this.setPerception(attributes3[2]);
				break;
		}
		//random increase of will
		this.setWill(this.getWill() + this.rollDices(6));
	}
		
	/**
	 * roll attributes limited by the points parameters
	 * 
	 * @param points
	 * @return
	 */
	private int[] rollAttributesValue(int points)
	{
		int[] attr = new int[]{0,0,0};

		while((attr[0]+attr[1]+attr[2]) != points)
		{
			//first attribute
			attr[0] = this.rollDices(5);
			//second attribute
			if(points - attr[0] >= 4){
				attr[1] = this.rollDices(5);
			}else{
				if(((points+1)-attr[1])<=0){
					attr[1] = 0;
				}else{
					attr[1] = this.rollDices((points+1) - attr[1]);
				}
			}
			//third attribute
			if((points-(attr[0]+attr[1])) >= 4)
			{
				attr[2] = this.rollDices(5);
			}else{
				if(((points+1) - (attr[0]+attr[1]))<=0)
				{
					attr[2] = 0;
				}else{
					attr[2] = this.rollDices((points+1) - (attr[0]+attr[1]));
				}
			}
		}
		return attr;
	}
	
	/**
	 * defines the priority of the three types of attributes
	 */
	private void definePriotity(){
		int priority = 3;
		while(priority > 0){
			this.priority.add(this.attributes.remove(this.rollDices(priority)));
			priority--;
		}
	}
	
	
	
	/**
	 * returns the modifier to the dices tests of the simulator
	 * @return
	 */
	public Integer getModifier(){
		switch(this.hp)
		{
			//bruise
			case 6:
			//injured
			case 5:
				return 1;
			//seriously injured
			case 4:
			//beaten
			case 3:
				return 2;
			//crippled
			case 2:
				return 5;
			//disabled
			case 1:
				return Integer.MAX_VALUE;
			//scarred or normal
			default:
				return 0;		
		}
	}
	
	/**
	 * <ul>
	 * <li>validate field value</li>
	 * <li>field values can't be lower than 1 and higher than 5</li>
	 * </ul>
	 * 
	 * @param fieldValue
	 * @return
	 */
	@SuppressWarnings("unused")
	private Boolean validateAttribute(int fieldValue)
	{
		if(fieldValue < 1)
		{
			return false;
		}
		if(fieldValue > 5)
		{
			return false;
		}
		return true;
	}
	
	/**
	 * constructor used to search humans in lists
	 * @param id
	 */
	public Human(Integer id)
	{
		super(id);
	}
	
	public int getStrength() {
		return strength;
	}
	private void setStrength(int strength) {
		this.strength = strength;
	}
	public int getDexterity() {
		return dexterity;
	}
	private void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}
	public int getStamina() {
		return stamina;
	}
	private void setStamina(int stamina) {
		this.stamina = stamina;
	}
	public int getCharisma() {
		return charisma;
	}
	private void setCharisma(int charisma) {
		this.charisma = charisma;
	}
	public int getAppearance() {
		return appearance;
	}
	private void setAppearance(int appearance) {
		this.appearance = appearance;
	}
	public int getLeadership() {
		return leadership;
	}
	private void setLeadership(int leadership) {
		this.leadership = leadership;
	}
	public int getIntelligence() {
		return intelligence;
	}
	private void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}
	public int getReasoning() {
		return reasoning;
	}
	private void setReasoning(int reasoning) {
		this.reasoning = reasoning;
	}
	public int getPerception() {
		return perception;
	}
	private void setPerception(int perception) {
		this.perception = perception;
	}
	public int getWill() {
		return will;
	}
	public void setWill(int will) {
		this.will = will;
	}
	public int getHp() {
		return hp;
	}
	public void setHp(int hp) {
		this.hp = hp;
	}

	public ArrayList<String> getAttributes() {
		return attributes;
	}

	public ArrayList<String> getPriority() {
		return priority;
	}

	@Override
	public void act(int time) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Message> getEar() {
		return ear;
	}

	public void setEar(ArrayList<Message> ear) {
		this.ear = ear;
	}
	
	public void logger(){
		HumanLogger.logHuman(this);
	}
	
	public void header(){
		HumanLogger.saveHeader();
	}

	/**
	 * send voice message
	 * @param mouth
	 * @throws SimulatorException 
	 */
	protected void sendVoiceMessage(Message message) throws SimulatorException {
		if(message.getType() != MessageType.VOICE)
			throw new SimulatorException(Transmitter.getProperty("exception.properties", "exception.not.voice.message"));
		this.getVoice().add(message);
	}

	public Double getOmega() {
		return omega;
	}

	public void setOmega(Double omega) {
		this.omega = omega;
	}

	
	

}
