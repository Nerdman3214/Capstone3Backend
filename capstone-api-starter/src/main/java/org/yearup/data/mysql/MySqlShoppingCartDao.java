package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.Product;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao
{
    public MySqlShoppingCartDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public ShoppingCart getByUserId(int userId)
    {
        ShoppingCart cart = new ShoppingCart();

        String sql = "SELECT sc.quantity, p.* " +
                     " FROM shopping_cart sc " +
                     " JOIN products p ON sc.product_id = p.product_id " +
                     " WHERE sc.user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery())
            {
                while (rs.next())
                {
                    Product product = MySqlProductDao.mapRow(rs);
                    int quantity = rs.getInt("quantity");

                    ShoppingCartItem item = new ShoppingCartItem();
                    item.setProduct(product);
                    item.setQuantity(quantity);

                    cart.add(item);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return cart;
    }

    @Override
    public void addItem(int userId, int productId, int quantity)
    {
        String checkSql = "SELECT quantity FROM shopping_cart WHERE user_id = ? AND product_id = ?";
        String insertSql = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?, ?, ?)";
        String updateSql = "UPDATE shopping_cart SET quantity = quantity + ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection())
        {
            try (PreparedStatement check = connection.prepareStatement(checkSql))
            {
                check.setInt(1, userId);
                check.setInt(2, productId);

                try (ResultSet rs = check.executeQuery())
                {
                    if (rs.next())
                    {
                        try (PreparedStatement update = connection.prepareStatement(updateSql))
                        {
                            update.setInt(1, quantity);
                            update.setInt(2, userId);
                            update.setInt(3, productId);
                            update.executeUpdate();
                        }
                    }
                    else
                    {
                        try (PreparedStatement insert = connection.prepareStatement(insertSql))
                        {
                            insert.setInt(1, userId);
                            insert.setInt(2, productId);
                            insert.setInt(3, quantity);
                            insert.executeUpdate();
                        }
                    }
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(int userId, int productId, int quantity)
    {
        if (quantity <= 0)
        {
            String deleteSql = "DELETE FROM shopping_cart WHERE user_id = ? AND product_id = ?";
            try (Connection connection = getConnection();
                 PreparedStatement ps = connection.prepareStatement(deleteSql))
            {
                ps.setInt(1, userId);
                ps.setInt(2, productId);
                ps.executeUpdate();
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }
            return;
        }

        String sql = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearCart(int userId)
    {
        String sql = "DELETE FROM shopping_cart WHERE user_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }
}