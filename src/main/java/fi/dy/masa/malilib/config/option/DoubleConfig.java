package fi.dy.masa.malilib.config.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.util.math.MathHelper;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.config.SliderConfig;

public class DoubleConfig extends BaseSliderConfig<Double> implements SliderConfig
{
    protected final double defaultValue;
    protected double value;
    protected double lastSavedValue;
    protected double minValue;
    protected double maxValue;

    public DoubleConfig(String name, double defaultValue)
    {
        this(name, defaultValue, name);
    }

    public DoubleConfig(String name, double defaultValue, String comment)
    {
        this(name, defaultValue, Double.MIN_VALUE, Double.MAX_VALUE, comment);
    }

    public DoubleConfig(String name, double defaultValue, double minValue, double maxValue)
    {
        this(name, defaultValue, minValue, maxValue, name);
    }

    public DoubleConfig(String name, double defaultValue, double minValue, double maxValue, String comment)
    {
        this(name, defaultValue, minValue, maxValue, false, comment);
    }

    public DoubleConfig(String name, double defaultValue, double minValue, double maxValue, boolean sliderActive, String comment)
    {
        super(name, comment, sliderActive);

        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;

        this.cacheSavedValue();
    }

    public double getDoubleValue()
    {
        return this.value;
    }

    public float getFloatValue()
    {
        return (float) this.getDoubleValue();
    }

    public double getDefaultDoubleValue()
    {
        return this.defaultValue;
    }

    public void setDoubleValue(double value)
    {
        double oldValue = this.value;
        this.value = this.getClampedValue(value);

        if (oldValue != this.value)
        {
            this.onValueChanged(value, oldValue);
        }
    }

    public double getMinDoubleValue()
    {
        return this.minValue;
    }

    public double getMaxDoubleValue()
    {
        return this.maxValue;
    }

    public void setMinDoubleValue(double minValue)
    {
        this.minValue = minValue;
        this.setDoubleValue(this.value);
    }

    public void setMaxDoubleValue(double maxValue)
    {
        this.maxValue = maxValue;
        this.setDoubleValue(this.value);
    }

    protected double getClampedValue(double value)
    {
        return MathHelper.clamp(value, this.minValue, this.maxValue);
    }

    @Override
    public boolean isModified()
    {
        return this.value != this.defaultValue;
    }

    public boolean isModified(String newValue)
    {
        try
        {
            return Double.parseDouble(newValue) != this.defaultValue;
        }
        catch (Exception ignore)
        {
        }

        return true;
    }

    @Override
    public boolean isDirty()
    {
        return this.lastSavedValue != this.value;
    }

    @Override
    public void cacheSavedValue()
    {
        this.lastSavedValue = this.value;
    }

    @Override
    public void resetToDefault()
    {
        this.setDoubleValue(this.defaultValue);
    }

    public String getStringValue()
    {
        return String.valueOf(this.value);
    }

    public String getDefaultStringValue()
    {
        return String.valueOf(this.defaultValue);
    }

    public void setValueFromString(String value)
    {
        try
        {
            this.setDoubleValue(Double.parseDouble(value));
        }
        catch (Exception e)
        {
            MaLiLib.LOGGER.warn("Failed to set config value for {} from the string '{}'", this.getName(), value);
        }
    }

    @Override
    public void setValueFromJsonElement(JsonElement element, String configName)
    {
        try
        {
            if (element.isJsonPrimitive())
            {
                this.value = this.getClampedValue(element.getAsDouble());
                this.onValueLoaded(this.value);
            }
            else
            {
                MaLiLib.LOGGER.warn("Failed to set config value for '{}' from the JSON element '{}'", configName, element);
            }
        }
        catch (Exception e)
        {
            MaLiLib.LOGGER.warn("Failed to set config value for '{}' from the JSON element '{}'", configName, element, e);
        }

        this.cacheSavedValue();
    }

    @Override
    public JsonElement getAsJsonElement()
    {
        return new JsonPrimitive(this.value);
    }
}