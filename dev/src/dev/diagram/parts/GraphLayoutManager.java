package dev.diagram.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;

public class GraphLayoutManager extends AbstractLayout
{

	private ContentsModelPart contentsModelPart;

	public GraphLayoutManager(ContentsModelPart contentsModelPart)
	{
		this.contentsModelPart = contentsModelPart;
	}

	@Override
	public void layout(IFigure container)
	{
		
		DirectedGraph graph = new DirectedGraph();
		Map<?, ?> partsToNodes = new HashMap<Object, Object>();
		contentsModelPart.contributeNodesToGraph(graph, partsToNodes);
		contentsModelPart.contributeEdgesToGraph(graph, partsToNodes);
		new DirectedGraphLayout().visit(graph);
		contentsModelPart.applyGraphResults(graph, partsToNodes);

	}

	@Override
	protected Dimension calculatePreferredSize(IFigure container, int wHint,
			int hHint)
	{
		
		container.validate();
		List<?> children = container.getChildren();
		Rectangle result = new Rectangle().setLocation(container
				.getClientArea().getLocation());
		for (int i = 0; i < children.size(); i++)
		{
			result.union(((IFigure) children.get(i)).getBounds());

		}

		result.resize(container.getInsets().getWidth(), container.getInsets()
				.getHeight());
		return result.getSize();

	}

}
