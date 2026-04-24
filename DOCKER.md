# Database Setup (Docker)

## Connection Details

| Field    | Value       |
|----------|-------------|
| Host     | `localhost` |
| Port     | `5432`      |
| Database | `yumi_blog_developments` |
| User     | `yumi`                   |
| Password | `yumiblog`               |

## Commands

**Start the database:**
```bash
docker compose up -d
```

**Stop the database:**
```bash
docker compose down
```

**Check if it's running:**
```bash
docker compose ps
```

## Connecting with pgAdmin

1. Open **pgAdmin**
2. Right-click **Servers** → **Register** → **Server**
3. In the **General** tab, set **Name** to anything (e.g. `yumi-blog`)
4. In the **Connection** tab, fill in:
   - **Host:** `localhost`
   - **Port:** `5432`
   - **Database:** `yumi_blog_developments`
   - **Username:** `yumi`
   - **Password:** `yumiblog`
5. Click **Save**






