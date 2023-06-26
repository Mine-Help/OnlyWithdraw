package me.cable.onlywithdraw.util;

import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public final class BigDecimalPersistentDataType implements PersistentDataType<String, BigDecimal> {

    public static final BigDecimalPersistentDataType INSTANCE = new BigDecimalPersistentDataType();

    private BigDecimalPersistentDataType() {}

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    @Override
    public @NotNull Class<BigDecimal> getComplexType() {
        return BigDecimal.class;
    }

    @Override
    public @NotNull String toPrimitive(BigDecimal complex, @NotNull PersistentDataAdapterContext context) {
        return complex.toPlainString();
    }

    @Override
    public @NotNull BigDecimal fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return new BigDecimal(primitive);
    }
}
