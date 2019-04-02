package CPlugin.view.Figures;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class is the DRAW of every box in the Graph
 * @author armando-cinvestav
 *
 */
public class AreaFigure extends Figure {
	
	
	private Label label;
	private RoundedRectangle rectangle;
	private ConnectionAnchor connectionAnchor;
	
	
	public AreaFigure() {
		setLayoutManager(new XYLayout());
		rectangle = new RoundedRectangle();
		rectangle.setBackgroundColor(ColorConstants.blue);
		//rectangle.setBounds(getBounds());
		rectangle.setLineWidth(0);
		add(rectangle);
		label = new Label();
		label.setForegroundColor(ColorConstants.white);
		add(label);
	}
	
	public ConnectionAnchor getConnectionAnchor() {
		if (connectionAnchor == null) {
			connectionAnchor = new ChopboxAnchor(this);
		}
		return connectionAnchor;
	}
	
	
	public Label getLabel() {
		return label;
	}
 
	public void paintFigure(Graphics g) {
		Rectangle r = getBounds().getCopy();
		setConstraint(rectangle, new Rectangle(0, 0, r.width, r.height));
		setConstraint(label, new Rectangle(0, 0, r.width, r.height));
		rectangle.invalidate();
		label.invalidate();
	}
}