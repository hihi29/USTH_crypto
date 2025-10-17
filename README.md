# USTH Crypto App
![app_logo.png](app/src/main/res/drawable/app_logo.png)
## Overview
USTH Crypto App is an Android application that visualises cryptocurrency market data using the public CoinGecko API. The app provides three primary screens:

- Home – Displays a ranked list of cryptocurrencies with live pricing and short-term performance metrics.
- Overview – Shows a price chart and metadata for a selected coin, allowing users to switch the time range between 1 day, 7 days, and 30 days.
- Exchange – Lists the most active crypto exchanges and provides quick links to their official websites.

## Group Members
| Student Name         | Student ID | Student Email                  |
| -------------------  | ---------- | ----------------------------   |
| Phạm Minh Trí        | 22BA13304  | tripm.22ba13304@usth.edu.vn    |
| Trần Quốc Thái       | 22BI14396  | thaitq.23bi14396@usth.edu.vn   |
| Nguyễn Đăng Khôi     | BI12-217   | khoind.bi12-217@st.usth.edu.vn |
| Trần Việt Hùng       | 22BA13149  | hungtv.22ba13149@usth.edu.vn   |
| Phạm Nguyễn Ngọc Hải | 22BA13123  | haipnn.22ba13123@usth.edu.vn   |
| Nguyễn Minh Tiến     | 22BA13298  | tiennm.22ba13298@usth.edu.vn   |

## API Usage
The app relies on three CoinGecko REST endpoints. All requests are performed with the Volley networking library and parse JSON responses into local model objects.

### 1. Market Listings
- Endpoint: `GET /api/v3/coins/markets`
- Purpose: Populate the Home screen with the top 100 cryptocurrencies ordered by market capitalisation.
- Key Query Parameters:
  - `vs_currency=usd` – Prices are returned in USD.
  - `order=market_cap_desc` – Sort by descending market cap.
  - `per_page=100` and `page=1` – Limit the response to the first 100 coins.
  - `sparkline=false` – Exclude historical sparkline data for lighter payloads.
  - `price_change_percentage=1h,24h,7d` – Include percentage changes for multiple windows.
- Client Usage: Parsed inside the Home fragment into `CoinData` objects that store rank, name, symbol, price, 1h/24h/7d changes, icon URL, and a reusable coin ID for detail screens.

### 2. Historical Market Chart
- Endpoint: `GET /api/v3/coins/{id}/market_chart`
- Purpose: Fetch time-series price data for the Overview chart when users switch between 1D, 7D, and 30D views.
- Path Parameter:
  - `{id}` – Coin identifier derived from the Home list selection (defaults to `bitcoin` when none selected).
- Key Query Parameters:
  - `vs_currency=usd` – Price series in USD.
  - `days={1|7|30}` – Number of trailing days requested, based on the selected filter.
- Client Usage: Results are cached in-memory per time range to avoid redundant network calls, then rendered as a LineChart. The Overview fragment adapts chart colours based on short-term price movement and updates textual metadata (current price, percentage change, and high/low where available).

### 3. Exchange Directory
- Endpoint: `GET /api/v3/exchanges`
- Purpose: Supply the Exchange screen with major trading venues, including location, 24h volume, and website link.
- Key Query Parameters:
  - `per_page=50` – Limit to the top 50 exchanges.
  - `page=1` – First page of results.
- Client Usage: Parsed into `ExchangeData` objects and displayed via `ExchangeAdapter`. Selecting a row opens the exchange URL in the device browser.

## Data Models
- `CoinData` – Immutable data holder for coin list entries. Includes fields for rank, name, symbol, price, hourly/24h/7d percentage changes, icon URL, and coin ID for further API calls.
- `ExchangeData` – Immutable data holder for exchange metadata: rank, name, country, 24h trading volume, website URL, and logo image URL.

## Running the App
1. Open the project in Android Studio (Giraffe or newer recommended).
2. Allow Gradle to download dependencies.
3. Connect an Android device or start an emulator.
4. Press Run to install and launch the application.

Note: CoinGecko enforces rate limiting on their free API; avoid rapidly spamming filter buttons or repeatedly refreshing data to prevent temporary blocks.
