package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;

import javax.swing.JFrame;

import agents.BotX;
import agents.BotY;
import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.field.continuous.Continuous3D;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.network.NetworkPortrayal2D;
import sim.portrayal.network.SimpleEdgePortrayal2D;
import sim.portrayal.network.SpatialNetwork2D;
import sim.portrayal.simple.CircledPortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.portrayal.simple.MovablePortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.ShapePortrayal2D;
import simulationModel.SimuModel;

public class SimuModelWithUI extends GUIState{
	public Display2D display;
	public JFrame displayFrame;
	ContinuousPortrayal2D yardPortrayal = new ContinuousPortrayal2D();
	NetworkPortrayal2D BotPortrayal = new NetworkPortrayal2D();

	public static void main(String[] args)
	{
		SimuModelWithUI vid = new SimuModelWithUI();
		Console c = new Console(vid);
		c.setVisible(true);
	}

	public SimuModelWithUI() {
		super(new SimuModel(System.currentTimeMillis()));
		// TODO Auto-generated constructor stub
	}
	
	public static String getName() { return "MultiAgent Network Provider Simulation"; }
	
	public Object getSimulationInspectedObject() { return state; }
	
	public Inspector getInspector()
	{
		Inspector i = super.getInspector();
		i.setVolatile(true);
		return i;
	}
	
	public static Object getInfo()
	{
		return "<h2>Multi Agent Network Simulation</h2>" +
				"<p>Enjoy it!";
	}
	
	public void start()
	{
		super.start();
		setupPortrayals();
	}
	
	public void load(SimState state)
	{
		super.load(state);
		setupPortrayals();
	}
	
	public void setupPortrayals()
	{
		SimuModel SM = (SimuModel) state;
		
		// tell the portrayals what to portray and how to portray them
		yardPortrayal.setField( SM.yard );
		yardPortrayal.setPortrayalForClass(agents.BotX.class, 
				new MovablePortrayal2D(
						new CircledPortrayal2D(
								new LabelledPortrayal2D(new ShapePortrayal2D(ShapePortrayal2D.X_POINTS_OCTAGON,ShapePortrayal2D.Y_POINTS_OCTAGON)
								{
									public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
									{
										BotX b = (BotX) object;
										paint = b.getColor();
										info.draw.height=15;
										info.draw.width=15;
										super.draw(object, graphics, info);
									}
								},
								5.0, null, Color.black, true),
								0, 5.0, Color.green, true))); 
		
		yardPortrayal.setPortrayalForClass(agents.BotY.class, 
				new MovablePortrayal2D(
						new CircledPortrayal2D(
								new LabelledPortrayal2D(new OvalPortrayal2D()
								{
									public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
									{
										BotY b = (BotY) object;
										paint = b.getColor();
										info.draw.height=10;
										info.draw.width=10;
										
										super.draw(object, graphics, info);
									}
								},
								5.0, null, Color.black, true),
								0, 5.0, Color.green, true))); 
				
		BotPortrayal.setField( new SpatialNetwork2D( SM.yard,SM.AllBotNetwork ) );
		BotPortrayal.setPortrayalForAll(new SimpleEdgePortrayal2D());

		

		// reschedule the displayer
		display.reset();
		display.setBackdrop(Color.white);
		// redraw the display
		display.repaint();
	}

	public void init(Controller c)
	{
		super.init(c);
		display = new Display2D(600,600,this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("Yard Display");
		c.registerFrame(displayFrame); // so the frame appears in the "Display" list
		displayFrame.setVisible(true);
		display.attach( BotPortrayal, "Buddies" );
		display.attach( yardPortrayal, "Yard" );
	}
	
	public void quit()
	{
		super.quit();
		if (displayFrame!=null) displayFrame.dispose();
		displayFrame = null;
		display = null;
	}

	
}
