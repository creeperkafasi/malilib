package fi.dy.masa.malilib.gui.widget;

import javax.annotation.Nullable;
import fi.dy.masa.malilib.gui.icon.Icon;
import fi.dy.masa.malilib.gui.util.ScreenContext;
import fi.dy.masa.malilib.util.data.EdgeInt;

public class IconWidget extends InteractableWidget
{
    protected boolean doHighlight;
    protected boolean enabled;
    protected int iconWidth;
    protected int iconHeight;

    public IconWidget(Icon icon)
    {
        super(-1, -1);

        this.setIcon(icon);
    }

    @Override
    public void setIcon(@Nullable Icon icon)
    {
        super.setIcon(icon);
        this.updateSize();
    }

    public IconWidget setUseEnabledVariant(boolean enabled)
    {
        this.enabled = enabled;
        return this;
    }

    public IconWidget setDoHighlight(boolean doHighlight)
    {
        this.doHighlight = doHighlight;
        return this;
    }

    @Override
    public void updateSize()
    {
        super.updateSize();

        this.iconWidth = this.getWidth() - this.getNonContentWidth();
        this.iconHeight = this.getHeight() - this.getNonContentHeight();
    }

    @Override
    public void renderAt(int x, int y, float z, ScreenContext ctx)
    {
        boolean hovered = this.doHighlight && this.isHoveredForRender(ctx);
        int color = this.getTextColorForRender(hovered);

        this.renderWidgetBackgroundAndBorder(x, y, z, ctx);
        this.renderText(x, y, z, color, ctx);

        if (this.getBackgroundRenderer().getNormalSettings().isEnabled())
        {
            int bw = this.getBorderRenderer().getNormalSettings().getActiveBorderWidth();
            EdgeInt padding = this.padding;
            x += padding.getLeft() + bw;
            y += padding.getTop() + bw;
        }

        this.renderIcon(x, y, z, this.enabled, hovered, ctx);
    }

    @Override
    protected void renderIcon(int x, int y, float z, boolean enabled, boolean hovered, ScreenContext ctx)
    {
        if (this.automaticWidth && this.automaticHeight)
        {
            super.renderIcon(x, y, z, enabled, hovered, ctx);
            return;
        }

        Icon icon = this.getIcon();

        if (icon != null)
        {
            int iconWidth = icon.getWidth();
            int iconHeight = icon.getHeight();
            int maxIconWidth = this.iconWidth;
            int maxIconHeight = this.iconHeight;

            if (iconWidth > maxIconWidth || iconHeight > maxIconHeight)
            {
                double widthScale = (double) maxIconWidth / (double) iconWidth;
                double heightScale = (double) maxIconHeight / (double) iconHeight;
                double scale = Math.min(widthScale, heightScale);
                iconWidth = (int) Math.floor(scale * iconWidth);
                iconHeight = (int) Math.floor(scale * iconHeight);
            }

            int usableWidth = this.getWidth() - this.getNonContentWidth();
            int usableHeight = this.getHeight() - this.getNonContentHeight();
            x = this.getIconPositionX(x, usableWidth, iconWidth);
            y = this.getIconPositionY(y, usableHeight, iconHeight);

            icon.renderScaledAt(x, y, z + 0.025f, iconWidth, iconHeight);
        }
    }
}
