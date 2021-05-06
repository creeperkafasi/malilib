package fi.dy.masa.malilib.gui.action;

import java.util.List;
import fi.dy.masa.malilib.gui.BaseScreen;
import fi.dy.masa.malilib.gui.icon.DefaultIcons;
import fi.dy.masa.malilib.gui.icon.Icon;
import fi.dy.masa.malilib.gui.icon.IconRegistry;
import fi.dy.masa.malilib.gui.position.EdgeInt;
import fi.dy.masa.malilib.gui.widget.BaseTextFieldWidget;
import fi.dy.masa.malilib.gui.widget.CheckBoxWidget;
import fi.dy.masa.malilib.gui.widget.ColorEditorWidget;
import fi.dy.masa.malilib.gui.widget.DropDownListWidget;
import fi.dy.masa.malilib.gui.widget.FloatEditWidget;
import fi.dy.masa.malilib.gui.widget.IconWidget;
import fi.dy.masa.malilib.gui.widget.IntegerEditWidget;
import fi.dy.masa.malilib.gui.widget.LabelWidget;
import fi.dy.masa.malilib.gui.widget.button.GenericButton;
import fi.dy.masa.malilib.util.StringUtils;
import fi.dy.masa.malilib.util.data.Vec2i;

public class EditActionExecutionWidgetScreen extends BaseScreen
{
    protected final List<BaseActionExecutionWidget> widgets;
    protected final BaseActionExecutionWidget firstWidget;
    protected final LabelWidget nameLabelWidget;
    protected final LabelWidget nameNormalColorLabelWidget;
    protected final LabelWidget nameHoveredColorLabelWidget;
    protected final LabelWidget nameXOffsetLabelWidget;
    protected final LabelWidget nameYOffsetLabelWidget;
    protected final LabelWidget iconLabelWidget;
    protected final LabelWidget iconXOffsetLabelWidget;
    protected final LabelWidget iconYOffsetLabelWidget;
    protected final LabelWidget iconScaleXLabelWidget;
    protected final LabelWidget iconScaleYLabelWidget;
    protected final LabelWidget hoveredBgColorLabelWidget;
    protected final LabelWidget normalBgColorLabelWidget;
    protected final LabelWidget hoveredBorderColorLabelWidget;
    protected final LabelWidget normalBorderColorLabelWidget;
    protected final BaseTextFieldWidget nameTextField;
    protected final DropDownListWidget<Icon> iconDropDownWidget;
    protected final IntegerEditWidget nameXOffsetEditWidget;
    protected final IntegerEditWidget nameYOffsetEditWidget;
    protected final IntegerEditWidget iconXOffsetEditWidget;
    protected final IntegerEditWidget iconYOffsetEditWidget;
    protected final FloatEditWidget iconScaleXEditWidget;
    protected final FloatEditWidget iconScaleYEditWidget;
    protected final CheckBoxWidget nameCenteredOnXCheckbox;
    protected final CheckBoxWidget nameCenteredOnYCheckbox;
    protected final CheckBoxWidget iconCenteredOnXCheckbox;
    protected final CheckBoxWidget iconCenteredOnYCheckbox;
    protected final ColorEditorWidget nameNormalColorEditWidget;
    protected final ColorEditorWidget nameHoveredColorEditWidget;
    protected final ColorEditorWidget hoveredBackgroundColorEditWidget;
    protected final ColorEditorWidget normalBackgroundColorEditWidget;
    protected final ColorEditorWidget hoveredBorderColorEditWidget;
    protected final ColorEditorWidget normalBorderColorEditWidget;
    protected final GenericButton cancelButton;
    protected final GenericButton removeIconButton;
    protected Vec2i dragStartOffset = Vec2i.ZERO;
    protected boolean dragging;
    protected boolean shouldApplyValues = true;

    public EditActionExecutionWidgetScreen(List<BaseActionExecutionWidget> widgets)
    {
        if (widgets.size() > 1)
        {
            this.title = StringUtils.translate("malilib.gui.title.edit_action_execution_widget.multiple", widgets.size());
            this.setScreenWidthAndHeight(240, 164);
        }
        else
        {
            this.title = StringUtils.translate("malilib.gui.title.edit_action_execution_widget");
            this.setScreenWidthAndHeight(240, 324);
        }

        this.useTitleHierarchy = false;
        this.widgets = widgets;
        BaseActionExecutionWidget widget = widgets.get(0);
        this.firstWidget = widget;

        this.nameLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.name_optional.colon");
        this.iconLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.icon_optional.colon");

        this.nameTextField = new BaseTextFieldWidget(0, 0, 140, 16, widget.getName());
        this.nameTextField.setListener(this.firstWidget::setName);

        this.iconDropDownWidget = new DropDownListWidget<>(0, 0, 120, 16, 120, 10,
                                                           IconRegistry.INSTANCE.getAllIcons(),
                                                           IconRegistry::getKeyForIcon, (x, y, h, i) -> new IconWidget(x, y, i));
        this.iconDropDownWidget.setSelectedEntry(widget.getIcon());
        this.iconDropDownWidget.setSelectionListener(this.firstWidget::setIcon);

        this.nameXOffsetLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.name_x_offset.colon");
        this.nameYOffsetLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.name_y_offset.colon");

        this.iconXOffsetLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.icon_x_offset.colon");
        this.iconYOffsetLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.icon_y_offset.colon");

        this.iconScaleXLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.icon_scale_x.colon");
        this.iconScaleYLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.icon_scale_y.colon");

        this.nameNormalColorLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.name_color_normal.colon");
        this.nameHoveredColorLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.name_color_hovered.colon");

        this.normalBgColorLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.background.colon");
        this.hoveredBgColorLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.hovered_background.colon");

        this.normalBorderColorLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.border_color.colon");
        this.hoveredBorderColorLabelWidget = new LabelWidget(0, 0, 0xFFFFFFFF, "malilib.label.hovered_border.colon");

        this.cancelButton = new GenericButton(0, 0, -1, 16, "malilib.gui.button.cancel");
        this.cancelButton.setActionListener(this::cancel);

        this.removeIconButton = GenericButton.createIconOnly(0, 0, DefaultIcons.LIST_REMOVE_MINUS_13);
        this.removeIconButton.translateAndAddHoverString("malilib.gui.button.label.remove_icon");
        this.removeIconButton.setActionListener(this::removeIcon);

        this.nameXOffsetEditWidget = new IntegerEditWidget(0, 0, 72, 16, widget.getTextOffsetX(), -512, 512, widget::setTextOffsetX);
        this.nameYOffsetEditWidget = new IntegerEditWidget(0, 0, 72, 16, widget.getTextOffsetY(), -512, 512, widget::setTextOffsetY);

        this.nameCenteredOnXCheckbox = new CheckBoxWidget(0, 0, "malilib.label.center", null);
        this.nameCenteredOnXCheckbox.setBooleanStorage(widget::getCenterTextHorizontally, widget::setCenterTextHorizontally);

        this.nameCenteredOnYCheckbox = new CheckBoxWidget(0, 0, "malilib.label.center", null);
        this.nameCenteredOnYCheckbox.setBooleanStorage(widget::getCenterTextVertically, widget::setCenterTextVertically);

        this.iconXOffsetEditWidget = new IntegerEditWidget(0, 0, 72, 16, widget.getIconOffsetX(), -512, 512, widget::setIconOffsetX);
        this.iconYOffsetEditWidget = new IntegerEditWidget(0, 0, 72, 16, widget.getIconOffsetY(), -512, 512, widget::setIconOffsetY);

        this.iconCenteredOnXCheckbox = new CheckBoxWidget(0, 0, "malilib.label.center", null);
        this.iconCenteredOnXCheckbox.setBooleanStorage(widget::getCenterIconHorizontally, widget::setCenterIconHorizontally);

        this.iconCenteredOnYCheckbox = new CheckBoxWidget(0, 0, "malilib.label.center", null);
        this.iconCenteredOnYCheckbox.setBooleanStorage(widget::getCenterIconVertically, widget::setCenterIconVertically);

        this.iconScaleXEditWidget = new FloatEditWidget(0, 0, 72, 16, widget.getIconScaleX(), 0, 100, widget::setIconScaleX);
        this.iconScaleYEditWidget = new FloatEditWidget(0, 0, 72, 16, widget.getIconScaleY(), 0, 100, widget::setIconScaleY);

        this.nameNormalColorEditWidget = new ColorEditorWidget(0, 0, 90, 16, widget::getDefaultNormalTextColor, widget::setDefaultNormalTextColor);
        this.nameHoveredColorEditWidget = new ColorEditorWidget(0, 0, 90, 16, widget::getDefaultHoveredTextColor, widget::setDefaultHoveredTextColor);

        this.normalBackgroundColorEditWidget    = new ColorEditorWidget(0, 0, 90, 16, widget::getNormalBackgroundColor, widget::setNormalBackgroundColor);
        this.hoveredBackgroundColorEditWidget   = new ColorEditorWidget(0, 0, 90, 16, widget::getHoveredBackgroundColor, widget::setHoveredBackgroundColor);

        this.normalBorderColorEditWidget        = new ColorEditorWidget(0, 0, 90, 16, widget.getNormalBorderColor());
        this.hoveredBorderColorEditWidget       = new ColorEditorWidget(0, 0, 90, 16, widget.getHoveredBorderColor());

        this.backgroundColor = 0xFF101010;
        this.centerOnScreen();
    }

    @Override
    protected void initScreen()
    {
        super.initScreen();

        this.updateWidgetPositions();

        if (this.widgets.size() == 1)
        {
            this.addWidget(this.nameLabelWidget);
            this.addWidget(this.nameTextField);

            this.addWidget(this.iconLabelWidget);
            this.addWidget(this.iconDropDownWidget);
            this.addWidget(this.removeIconButton);

            this.addWidget(this.nameXOffsetLabelWidget);
            this.addWidget(this.nameXOffsetEditWidget);
            this.addWidget(this.nameCenteredOnXCheckbox);

            this.addWidget(this.nameYOffsetLabelWidget);
            this.addWidget(this.nameYOffsetEditWidget);
            this.addWidget(this.nameCenteredOnYCheckbox);

            this.addWidget(this.iconXOffsetLabelWidget);
            this.addWidget(this.iconXOffsetEditWidget);
            this.addWidget(this.iconCenteredOnXCheckbox);

            this.addWidget(this.iconYOffsetLabelWidget);
            this.addWidget(this.iconYOffsetEditWidget);
            this.addWidget(this.iconCenteredOnYCheckbox);

            this.addWidget(this.iconScaleXLabelWidget);
            this.addWidget(this.iconScaleYLabelWidget);

            this.addWidget(this.iconScaleXEditWidget);
            this.addWidget(this.iconScaleYEditWidget);
        }

        this.addWidget(this.nameNormalColorLabelWidget);
        this.addWidget(this.nameNormalColorEditWidget);

        this.addWidget(this.nameHoveredColorLabelWidget);
        this.addWidget(this.nameHoveredColorEditWidget);

        this.addWidget(this.normalBgColorLabelWidget);
        this.addWidget(this.normalBackgroundColorEditWidget);

        this.addWidget(this.hoveredBgColorLabelWidget);
        this.addWidget(this.hoveredBackgroundColorEditWidget);

        this.addWidget(this.normalBorderColorLabelWidget);
        this.addWidget(this.normalBorderColorEditWidget);

        this.addWidget(this.hoveredBorderColorLabelWidget);
        this.addWidget(this.hoveredBorderColorEditWidget);

        this.addWidget(this.cancelButton);
    }

    protected void updateWidgetPositions()
    {
        int x = this.x + 10;
        int y = this.y + 24;

        if (this.widgets.size() == 1)
        {
            this.nameLabelWidget.setPosition(x, y + 4);
            this.nameTextField.setPosition(this.nameLabelWidget.getRight() + 6, y);
            y += 20;

            this.iconLabelWidget.setPosition(x, y + 4);
            this.iconDropDownWidget.setPosition(this.iconLabelWidget.getRight() + 6, y);
            this.removeIconButton.setPosition(this.iconDropDownWidget.getRight() + 2, y + 1);
            y += 20;

            this.nameXOffsetLabelWidget.setPosition(x, y + 4);
            this.nameXOffsetEditWidget.setPosition(this.nameXOffsetLabelWidget.getRight() + 6, y);
            this.nameCenteredOnXCheckbox.setPosition(this.nameXOffsetEditWidget.getRight() + 6, y + 2);
            y += 20;

            this.nameYOffsetLabelWidget.setPosition(x, y + 4);
            this.nameYOffsetEditWidget.setPosition(this.nameYOffsetLabelWidget.getRight() + 6, y);
            this.nameCenteredOnYCheckbox.setPosition(this.nameYOffsetEditWidget.getRight() + 6, y + 2);
            y += 20;

            this.iconXOffsetLabelWidget.setPosition(x, y + 4);
            this.iconXOffsetEditWidget.setPosition(this.iconXOffsetLabelWidget.getRight() + 6, y);
            this.iconCenteredOnXCheckbox.setPosition(this.iconXOffsetEditWidget.getRight() + 6, y + 2);
            y += 20;

            this.iconYOffsetLabelWidget.setPosition(x, y + 4);
            this.iconYOffsetEditWidget.setPosition(this.iconYOffsetLabelWidget.getRight() + 6, y);
            this.iconCenteredOnYCheckbox.setPosition(this.iconYOffsetEditWidget.getRight() + 6, y + 2);
            y += 20;

            this.iconScaleXLabelWidget.setPosition(x, y + 4);
            this.iconScaleXEditWidget.setPosition(this.iconScaleXLabelWidget.getRight() + 6, y);
            y += 20;

            this.iconScaleYLabelWidget.setPosition(x, y + 4);
            this.iconScaleYEditWidget.setPosition(this.iconScaleYLabelWidget.getRight() + 6, y);
            y += 20;
        }

        this.nameNormalColorLabelWidget.setPosition(x, y + 4);
        this.nameNormalColorEditWidget.setY(y);
        y += 20;

        this.nameHoveredColorLabelWidget.setPosition(x, y + 4);
        this.nameHoveredColorEditWidget.setY(y);
        y += 20;

        this.normalBgColorLabelWidget.setPosition(x, y + 4);
        this.normalBackgroundColorEditWidget.setY(y);
        y += 20;

        this.hoveredBgColorLabelWidget.setPosition(x, y + 4);
        this.hoveredBackgroundColorEditWidget.setY(y);
        y += 20;

        this.normalBorderColorLabelWidget.setPosition(x, y + 4);
        this.normalBorderColorEditWidget.setY(y);
        y += 20;

        this.hoveredBorderColorLabelWidget.setPosition(x, y + 4);
        this.hoveredBorderColorEditWidget.setY(y);

        int x1 = Math.max(this.nameNormalColorLabelWidget.getRight(), this.nameHoveredColorLabelWidget.getRight());
        int x2 = Math.max(this.normalBgColorLabelWidget.getRight(), this.hoveredBgColorLabelWidget.getRight());
        int x3 = Math.max(this.normalBorderColorLabelWidget.getRight(), this.hoveredBorderColorLabelWidget.getRight());
        x = Math.max(x1, x2);
        x = Math.max(x, x3) + 6;
        this.nameNormalColorEditWidget.setX(x);
        this.nameHoveredColorEditWidget.setX(x);
        this.normalBackgroundColorEditWidget.setX(x);
        this.hoveredBackgroundColorEditWidget.setX(x);
        this.normalBorderColorEditWidget.setX(x);
        this.hoveredBorderColorEditWidget.setX(x);

        y += 20;
        this.cancelButton.setPosition(this.x + 10, y);
    }

    @Override
    public void onGuiClosed()
    {
        super.onGuiClosed();

        this.applyValues();
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (super.onMouseClicked(mouseX, mouseY, mouseButton))
        {
            return true;
        }

        this.dragStartOffset = new Vec2i(mouseX - this.getX(),  mouseY - this.getY());
        this.dragging = true;

        return true;
    }

    @Override
    public boolean onMouseMoved(int mouseX, int mouseY)
    {
        if (this.dragging)
        {
            int x = mouseX - this.dragStartOffset.x;
            int y = mouseY - this.dragStartOffset.y;

            this.setPosition(x, y);
            this.updateWidgetPositions();

            return true;
        }

        return super.onMouseMoved(mouseX, mouseY);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton)
    {
        this.dragging = false;
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    protected void cancel()
    {
        this.shouldApplyValues = false;
        this.closeScreen(true);
    }

    protected void removeIcon()
    {
        this.firstWidget.setIcon(null);
        this.iconDropDownWidget.setSelectedEntry(null);
    }

    protected void applyValues()
    {
        if (this.shouldApplyValues == false)
        {
            return;
        }

        int size = this.widgets.size();

        // Copy the values from the first widget, to which they get set from the edit widgets
        if (size > 1)
        {
            int normalNameColor = this.firstWidget.getDefaultNormalTextColor();
            int hoveredNameColor = this.firstWidget.getDefaultHoveredTextColor();
            int normalBg = this.firstWidget.getNormalBackgroundColor();
            int hoverBg = this.firstWidget.getHoveredBackgroundColor();
            EdgeInt normalBorder = this.firstWidget.getNormalBorderColor();
            EdgeInt hoverBorder = this.firstWidget.getHoveredBorderColor();

            for (int i = 1; i < size; ++i)
            {
                BaseActionExecutionWidget widget = this.widgets.get(i);
                widget.setDefaultNormalTextColor(normalNameColor);
                widget.setDefaultHoveredTextColor(hoveredNameColor);

                widget.setNormalBackgroundColor(normalBg);
                widget.setHoveredBackgroundColor(hoverBg);

                widget.getNormalBorderColor().setFrom(normalBorder);
                widget.getHoveredBorderColor().setFrom(hoverBorder);
            }
        }
    }
}
