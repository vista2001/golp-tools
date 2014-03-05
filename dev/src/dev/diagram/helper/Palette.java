package dev.diagram.helper;

import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import dev.Application;
import dev.diagram.model.AopModel;
import dev.diagram.model.ConditionModel;
import dev.diagram.model.EndModel;
import dev.diagram.model.LineConnectionModel;
import dev.diagram.model.ReturnModel;
import dev.diagram.model.StartModel;
import dev.diagram.model.TfmModel;

/**
 * 创建调色板
 * 
 * @author 木木
 * 
 */
public class Palette {
	public static PaletteRoot getPaletteRoot() {

		PaletteRoot root = new PaletteRoot();
		PaletteGroup toolGroup = new PaletteGroup("工具");
		ToolEntry tool = new SelectionToolEntry();
		toolGroup.add(tool);
		root.setDefaultEntry(tool);
		tool = new MarqueeToolEntry();
		toolGroup.add(tool);
		PaletteDrawer drawer = new PaletteDrawer("画图");
		ImageDescriptor descripto_draw = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						"icons/small_event.gif");
		CreationToolEntry creationEntry = new CreationToolEntry("开始", "流程图开始块",
				new SimpleFactory(StartModel.class), descripto_draw,
				descripto_draw);
		drawer.add(creationEntry);
		ImageDescriptor descriptor_condition = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						"icons/rhombus.gif");
		CreationToolEntry creationEntry1 = new CreationToolEntry("条件", "创建条件",
				new SimpleFactory(ConditionModel.class), descriptor_condition,
				descriptor_condition);
		drawer.add(creationEntry1);
		ImageDescriptor descriptor_aop = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						"icons/rectangle.gif");
		CreationToolEntry creationEntry2 = new CreationToolEntry("AOP", "AOP",
				new SimpleFactory(AopModel.class), descriptor_aop,
				descriptor_aop);
		drawer.add(creationEntry2);
		ImageDescriptor descriptor_re = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						"icons/ellipse.gif");
		CreationToolEntry creationEntry3 = new CreationToolEntry("返回", "返回",
				new SimpleFactory(ReturnModel.class), descriptor_re,
				descriptor_re);
		drawer.add(creationEntry3);
		ImageDescriptor descriptor_tfm = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						"icons/maximize.gif");
		CreationToolEntry creationEntry4 = new CreationToolEntry("TFM",
				"创建TFM", new SimpleFactory(TfmModel.class), descriptor_tfm,
				descriptor_tfm);
		drawer.add(creationEntry4);
		ImageDescriptor descriptor_end = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						"icons/small_terminate.gif");
		CreationToolEntry creationEntry5 = new CreationToolEntry("结束", "结束",
				new SimpleFactory(EndModel.class), descriptor_end,
				descriptor_end);
		drawer.add(creationEntry5);
		ImageDescriptor descriptor_conn = AbstractUIPlugin
				.imageDescriptorFromPlugin(Application.PLUGIN_ID,
						IImageKeys.CONNECTION);
		PaletteDrawer drawer_c = new PaletteDrawer("连接");
		ConnectionCreationToolEntry connxCreationEntry = new ConnectionCreationToolEntry(
				"连接图形", "连接", new SimpleFactory(LineConnectionModel.class),
				descriptor_conn, descriptor_conn);
		drawer_c.add(connxCreationEntry);
		root.add(toolGroup);
		root.add(drawer);
		root.add(drawer_c);
		return root;
	}
}
